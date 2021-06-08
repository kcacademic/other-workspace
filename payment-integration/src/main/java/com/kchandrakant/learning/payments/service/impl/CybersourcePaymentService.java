package com.kchandrakant.learning.payments.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kchandrakant.learning.payments.client.CybersourceClient;
import com.kchandrakant.learning.payments.constant.PaymentConstant;
import com.kchandrakant.learning.payments.domain.PaymentByRequestId;
import com.kchandrakant.learning.payments.entity.Card;
import com.kchandrakant.learning.payments.entity.PaymentData;
import com.kchandrakant.learning.payments.event.PaymentEvent;
import com.kchandrakant.learning.payments.exception.PaymentCardMissingException;
import com.kchandrakant.learning.payments.exception.PaymentCreationException;
import com.kchandrakant.learning.payments.exception.PaymentExecutionException;
import com.kchandrakant.learning.payments.exception.PaymentRecordMissingException;
import com.kchandrakant.learning.payments.repository.PaymentRecordRepository;
import com.kchandrakant.learning.payments.repository.PaymentRequestRepository;
import com.kchandrakant.learning.payments.service.CardService;
import com.kchandrakant.learning.payments.service.PaymentService;
import com.kchandrakant.learning.payments.utility.TransformationUtility;

@Service("cybersourceService")
@PropertySource(value = "classpath:application.yml")
public class CybersourcePaymentService implements PaymentService {

    static Logger logger = LoggerFactory.getLogger(CybersourcePaymentService.class);

    @Autowired
    private CybersourceClient cybersourceIntegration;

    @Autowired
    private PaymentRecordRepository recordRepository;

    @Autowired
    private PaymentRequestRepository requestRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> broker;
    
    @Autowired
    TransformationUtility utility;

    @Value("${payments.kafka.topic}")
    private String topic;

    @Override
    public Optional<String> createPayment(PaymentData paymentData) throws PaymentCreationException {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<String> authenticatePayment(PaymentData paymentData) throws PaymentRecordMissingException {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<String> executePayment(PaymentData paymentData) throws PaymentRecordMissingException, PaymentExecutionException, PaymentCardMissingException {
        Optional<PaymentByRequestId> requestOptional = requestRepository.findById(paymentData.getIdempotencyKey());
        if (requestOptional.isPresent()) {
            return Optional.of(requestOptional.get()
                .getPaymentId());
        }

        if (paymentData.getCard()
            .getCardId() != null) {
            Optional<Card> cardOptional = cardService.getCard(paymentData.getUserId(), paymentData.getCard()
                .getCardId());
            if (cardOptional.isPresent()) {
                Card card = cardOptional.get();
                paymentData.getCard()
                    .setNumber(card.getNumber());
                paymentData.getCard()
                    .setExpirationYear(card.getExpirationYear());
                paymentData.getCard()
                    .setExpirationMonth(card.getExpirationMonth());
            }
        }

        PaymentData paymentResponse = cybersourceIntegration.executePayment(paymentData);

        if (PaymentConstant.TRANSACTION_FAILED.equals(paymentResponse.getState()))
            throw new PaymentExecutionException(String.format("Payment Creation Failed for id %s", paymentData.getIdempotencyKey()));

        recordRepository.save(utility.createPaymentRecordFromPaymentData(paymentResponse));

        requestRepository.save(utility.createPaymentRequestFromPaymentData(paymentResponse));

        if (paymentData.getCard()
            .isSaveCard()) {
            cardService.saveCard(paymentResponse.getUserId(), paymentResponse.getCard());
        }

        broker.send(topic, utility.createPaymentEvent(paymentResponse, PaymentConstant.PAYMENT_EXECUTED));

        return Optional.ofNullable(paymentResponse.getId());
    }

    @Override
    public Optional<String> capturePayment(PaymentData paymentData) throws PaymentRecordMissingException {
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<PaymentData> fetchPayment(PaymentData paymentData) throws PaymentRecordMissingException {
        return Optional.ofNullable(null);
    }

}