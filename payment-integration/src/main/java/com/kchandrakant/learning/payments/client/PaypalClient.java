package com.kchandrakant.learning.payments.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kchandrakant.learning.payments.entity.PaymentData;
import com.kchandrakant.learning.payments.exception.PaymentCreationException;
import com.kchandrakant.learning.payments.exception.PaymentExecutionException;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Component
public class PaypalClient {

    Logger logger = LoggerFactory.getLogger(PaypalClient.class);

    private static final String APPROVAL_URL = "approval_url";

    @Value("${payments.paypal.clientId}")
    private String clientId;

    @Value("${payments.paypal.clientSecret}")
    private String clientSecret;

    @Value("${payments.paypal.mode:sandbox}")
    private String mode;

    @Value("${payments.paypal.successUrl:http://localhost:8080/process}")
    private String successUrl;

    @Value("${payments.paypal.failureUrl:http://localhost:8080/cancel}")
    private String failureUrl;

    public PaymentData createPayment(PaymentData paymentData) throws PaymentCreationException {

        PaymentData paymentResponse = paymentData.toBuilder()
            .build();

        // Set payer details
        Payer payer = new Payer();
        payer.setPaymentMethod(paymentData.getPaymentMethod());

        // Set redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(failureUrl);
        redirectUrls.setReturnUrl(successUrl);

        // Set payment details
        Details details = new Details();
        details.setShipping(paymentData.getAmount()
            .getShipping());
        details.setSubtotal(paymentData.getAmount()
            .getSubTotal());
        details.setTax(paymentData.getAmount()
            .getTax());

        // Payment amount
        Amount amount = new Amount();
        amount.setCurrency(paymentData.getAmount()
            .getCurrency());
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal(paymentData.getAmount()
            .getTotal());
        amount.setDetails(details);

        // Transaction information
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(paymentData.getDescription());

        // Add transaction to a list
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // Add payment details
        Payment payment = new Payment();
        payment.setIntent(paymentData.getIntent());
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(transactions);

        // Create payment
        Payment createdPayment = null;
        String authUrl = null;
        try {
            APIContext apiContext = new APIContext(clientId, clientSecret, mode);
            apiContext.setRequestId(paymentData.getIdempotencyKey());
            createdPayment = payment.create(apiContext);

            Iterator<Links> links = createdPayment.getLinks()
                .iterator();
            while (links.hasNext()) {
                Links link = links.next();
                if (link.getRel()
                    .equalsIgnoreCase(APPROVAL_URL)) {
                    // Redirect the customer to link.getHref()
                    authUrl = link.getHref();
                    if (logger.isInfoEnabled())
                        logger.info(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            logger.error(e.getMessage());
        }

        if (createdPayment == null)
            throw new PaymentCreationException(String.format("Payment Creation Failed for id %s", paymentData.getIdempotencyKey()));

        paymentResponse.setId(createdPayment.getId());
        paymentResponse.setState(createdPayment.getState());
        paymentResponse.setAuthUrl(authUrl);

        return paymentResponse;
    }

    public String executePayment(String paymentId, String payerId) throws PaymentExecutionException {

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment createdPayment = null;
        try {
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            createdPayment = payment.execute(apiContext, paymentExecution);
            if (logger.isInfoEnabled())
                logger.info(createdPayment.toString());
        } catch (PayPalRESTException e) {
            logger.error(e.getMessage());
        }

        if (createdPayment == null)
            throw new PaymentExecutionException(String.format("Payment Execution Failed for id %s", paymentId));

        return createdPayment.getState();

    }

}
