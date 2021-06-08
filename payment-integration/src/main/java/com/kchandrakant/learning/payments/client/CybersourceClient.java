package com.kchandrakant.learning.payments.client;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cybersource.authsdk.core.ConfigException;
import com.cybersource.authsdk.core.MerchantConfig;
import com.kchandrakant.learning.payments.entity.PaymentData;
import com.kchandrakant.learning.payments.exception.PaymentExecutionException;

import Api.PaymentsApi;
import Invokers.ApiClient;
import Invokers.ApiException;
import Model.CreatePaymentRequest;
import Model.PtsV2PaymentsPost201Response;
import Model.Ptsv2paymentsClientReferenceInformation;
import Model.Ptsv2paymentsOrderInformation;
import Model.Ptsv2paymentsOrderInformationAmountDetails;
import Model.Ptsv2paymentsOrderInformationBillTo;
import Model.Ptsv2paymentsPaymentInformation;
import Model.Ptsv2paymentsPaymentInformationCard;
import Model.Ptsv2paymentsPointOfSaleInformation;
import Model.Ptsv2paymentsProcessingInformation;

@Component
public class CybersourceClient {

    Logger logger = LoggerFactory.getLogger(CybersourceClient.class);

    @Value("${payments.cybersource.merchandId:coc_cybersource}")
    private String merchandId;

    @Value("${payments.cybersource.keyAlias:coc_cybersource}")
    private String keyAlias;

    @Value("${payments.cybersource.keyPass:coc_cybersource}")
    private String keyPass;

    public PaymentData executePayment(PaymentData paymentData) throws PaymentExecutionException {

        PaymentData paymentResponse = paymentData.toBuilder()
            .build();

        PtsV2PaymentsPost201Response response = null;
        try {
            CreatePaymentRequest request = getRequest(paymentData);
            Properties merchantProp = getMerchantDetails();
            MerchantConfig merchantConfig = new MerchantConfig(merchantProp);
            ApiClient apiClient = new ApiClient();
            apiClient.merchantConfig = merchantConfig;
            PaymentsApi paymentApi = new PaymentsApi(apiClient);
            response = paymentApi.createPayment(request);

            if (logger.isInfoEnabled())
                logger.info(String.format("Response Code: %s and Status: %s", apiClient.responseCode, apiClient.status));

        } catch (ApiException | ConfigException e) {
            logger.error(e.getMessage());
        }

        if (response == null)
            throw new PaymentExecutionException(String.format("Payment Creation Failed for id %s", paymentData.getIdempotencyKey()));

        paymentResponse.setId(response.getId());
        paymentResponse.setState(response.getStatus());

        return paymentResponse;
    }

    private Properties getMerchantDetails() {
        Properties properties = new Properties();
        // HTTP_Signature = http_signature and JWT = jwt
        properties.setProperty("authenticationType", "jwt");
        properties.setProperty("merchantID", merchandId);
        properties.setProperty("runEnvironment", "CyberSource.Environment.SANDBOX");
        // JWT Parameters
        properties.setProperty("keyAlias", keyAlias);
        properties.setProperty("keyPass", keyPass);
        // P12 key path. Enter the folder path where the .p12 file is located.
        properties.setProperty("keysDirectory", "keys");
        // Logging to be enabled or not.
        properties.setProperty("enableLog", "true");
        properties.setProperty("logDirectory", "log");
        properties.setProperty("logFilename", "cybs");
        return properties;

    }

    private CreatePaymentRequest getRequest(PaymentData paymentData) {
        CreatePaymentRequest request = new CreatePaymentRequest();

        Ptsv2paymentsClientReferenceInformation client = new Ptsv2paymentsClientReferenceInformation();
        client.code("test_payment");
        request.clientReferenceInformation(client);

        Ptsv2paymentsPointOfSaleInformation saleInformation = new Ptsv2paymentsPointOfSaleInformation();
        saleInformation.catLevel(6);
        saleInformation.terminalCapability(4);
        request.pointOfSaleInformation(saleInformation);

        Ptsv2paymentsOrderInformationBillTo billTo = new Ptsv2paymentsOrderInformationBillTo();
        billTo.country(paymentData.getAddress()
            .getCountryCode());
        billTo.firstName(paymentData.getAddress()
            .getFirstName());
        billTo.lastName(paymentData.getAddress()
            .getLastName());
        billTo.address1(paymentData.getAddress()
            .getLine1());
        billTo.postalCode(paymentData.getAddress()
            .getPostCode());
        billTo.locality(paymentData.getAddress()
            .getCity());
        billTo.administrativeArea(paymentData.getAddress()
            .getState());
        billTo.email(paymentData.getAddress()
            .getEmail());

        Ptsv2paymentsOrderInformationAmountDetails amountDetails = new Ptsv2paymentsOrderInformationAmountDetails();
        amountDetails.totalAmount(paymentData.getAmount()
            .getTotal());
        amountDetails.currency(paymentData.getAmount()
            .getCurrency());

        Ptsv2paymentsOrderInformation orderInformation = new Ptsv2paymentsOrderInformation();
        orderInformation.billTo(billTo);
        orderInformation.amountDetails(amountDetails);
        request.setOrderInformation(orderInformation);

        Ptsv2paymentsProcessingInformation processingInformation = new Ptsv2paymentsProcessingInformation();
        processingInformation.capture(false);
        request.processingInformation(processingInformation);

        Ptsv2paymentsPaymentInformationCard card = new Ptsv2paymentsPaymentInformationCard();
        card.expirationYear(paymentData.getCard()
            .getExpirationYear());
        card.number(paymentData.getCard()
            .getNumber());
        card.securityCode(paymentData.getCard()
            .getSecurityCode());
        card.expirationMonth(paymentData.getCard()
            .getExpirationMonth());

        Ptsv2paymentsPaymentInformation paymentInformation = new Ptsv2paymentsPaymentInformation();
        paymentInformation.card(card);
        request.setPaymentInformation(paymentInformation);

        return request;

    }

}