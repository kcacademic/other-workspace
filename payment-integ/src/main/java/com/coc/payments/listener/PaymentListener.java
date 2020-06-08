package com.coc.payments.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.coc.payments.entity.Amount;
import com.coc.payments.entity.PaymentData;
import com.coc.payments.event.PaymentEvent;
import com.coc.payments.service.PaymentService;

@Component
public class PaymentListener {

    private static final String PAYMENT_AUTHENTICATED = "authenticated";

    Logger logger = LoggerFactory.getLogger(PaymentListener.class);

    @Autowired
    PaymentService paypalService;

    @KafkaListener(topics = "${payments.kafka.topic}", groupId = "${payments.kafka.groupid}")
    public void listen(PaymentEvent event) {
        if (logger.isInfoEnabled())
            logger.info(String.format("Received Messasge in group - coc_consumer: %s", event));
        if (PAYMENT_AUTHENTICATED.equals(event.getEventType())) {
            try {
                PaymentData paymentData = new PaymentData();
                paymentData.setId(event.getId());
                paymentData.setUserId(event.getUserId());
                paymentData.setPaymentProvider(event.getPaymentProvider());
                paymentData.setPaymentMethod(event.getPaymentMethod());
                Amount amount = new Amount();
                amount.setTotal(event.getAmount());
                paymentData.setAmount(amount);
                paypalService.executePayment(paymentData);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

}
