package com.coc.payments.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.coc.payments.client.PaypalClient;
import com.coc.payments.constant.PaymentConstant;
import com.coc.payments.domain.PaymentByPaymentId;
import com.coc.payments.domain.PaymentByRequestId;
import com.coc.payments.domain.TransactionType;
import com.coc.payments.entity.Amount;
import com.coc.payments.entity.PaymentData;
import com.coc.payments.event.PaymentEvent;
import com.coc.payments.exception.PaymentCreationException;
import com.coc.payments.exception.PaymentExecutionException;
import com.coc.payments.exception.PaymentRecordMissingException;
import com.coc.payments.repository.PaymentRecordRepository;
import com.coc.payments.repository.PaymentRequestRepository;
import com.coc.payments.service.PaymentService;
import com.coc.payments.utility.TransformationUtility;

@Service("paypalService")
@PropertySource(value = "classpath:application.yml")
public class PaypalPaymentService implements PaymentService {

    Logger logger = LoggerFactory.getLogger(PaypalPaymentService.class);

    @Autowired
    private PaypalClient paypalInteg;

    @Autowired
    private PaymentRecordRepository recordRepository;

    @Autowired
    private PaymentRequestRepository requestRepository;

    @Autowired
    private KafkaTemplate<String, PaymentEvent> broker;
    
    @Autowired
    TransformationUtility utility;

    @Value("${payments.kafka.topic}")
    private String topic;

    @Override
    public Optional<String> createPayment(PaymentData paymentData) throws PaymentCreationException {

        Optional<PaymentByRequestId> requestOptional = requestRepository.findById(paymentData.getIdempotencyKey());
        if (requestOptional.isPresent()) {
            PaymentByRequestId request = requestOptional.get();
            if (PaymentConstant.PAYMENT_AUTHENTICATED.equals(request.getPaymentStatus()) || PaymentConstant.PAYMENT_EXECUTED.equals(request.getPaymentStatus()))
                throw new PaymentCreationException(String.format("Payment with the key %s has already been processed.", paymentData.getIdempotencyKey()));
            return Optional.of(request.getAuthUrl());
        }

        PaymentData paymentResponse = paypalInteg.createPayment(paymentData);

        if (PaymentConstant.TRANSACTION_FAILED.equals(paymentResponse.getState()))
            throw new PaymentCreationException(String.format("Payment Creation Failed for key %s", paymentData.getIdempotencyKey()));

        recordRepository.save(utility.createPaymentRecordFromPaymentData(paymentResponse));

        requestRepository.save(utility.createPaymentRequestFromPaymentData(paymentResponse));

        broker.send(topic, utility.createPaymentEvent(paymentData, PaymentConstant.PAYMENT_CREATED));

        return Optional.ofNullable(paymentResponse.getAuthUrl());
    }

    @Override
    public Optional<String> authenticatePayment(PaymentData paymentData) throws PaymentRecordMissingException {

        Optional<PaymentByPaymentId> paymentOptional = recordRepository.findById(paymentData.getId());
        if (!paymentOptional.isPresent())
            throw new PaymentRecordMissingException(String.format("Payment Record Missing with Id %s", paymentData.getId()));

        PaymentByPaymentId record = paymentOptional.get();
        PaymentByPaymentId savedRecord = null;

        if (PaymentConstant.PAYMENT_CREATED.equals(record.getPaymentStatus())) {
            TransactionType transaction = new TransactionType(UUID.randomUUID(), PaymentConstant.PAYMENT_AUTHENTICATED, paymentData.getUserId(), paymentData.getAmount()
                .getTotal());
            record.setPayerId(paymentData.getPayerId());
            record.setPaymentStatus(PaymentConstant.PAYMENT_AUTHENTICATED);
            record.getTransactions()
                .add(transaction);
            savedRecord = recordRepository.save(record);

            Optional<PaymentByRequestId> requestOptional = requestRepository.findById(record.getIdempotencyKey());
            if (requestOptional.isPresent()) {
                PaymentByRequestId request = requestOptional.get();
                request.setPaymentStatus(PaymentConstant.PAYMENT_AUTHENTICATED);
                requestRepository.save(request);
            }
        }

        paymentData.setUserId(record.getUserId());
        Amount amount = new Amount();
        amount.setTotal(record.getAmount()
            .getTotal());
        paymentData.setAmount(amount);
        broker.send(topic, utility.createPaymentEvent(paymentData, PaymentConstant.PAYMENT_AUTHENTICATED));

        return Optional.ofNullable(savedRecord != null ? savedRecord.getId() : null);
    }

    @Override
    public Optional<String> executePayment(PaymentData paymentData) throws PaymentRecordMissingException, PaymentExecutionException {

        Optional<PaymentByPaymentId> paymentOptional = recordRepository.findById(paymentData.getId());
        if (!paymentOptional.isPresent())
            throw new PaymentRecordMissingException(String.format("Payment Record Missing with Id %s", paymentData.getId()));

        PaymentByPaymentId record = paymentOptional.get();

        PaymentByPaymentId savedRecord = null;

        if (PaymentConstant.PAYMENT_AUTHENTICATED.equals(record.getPaymentStatus())) {
            String state = paypalInteg.executePayment(record.getId(), record.getPayerId());
            if (PaymentConstant.TRANSACTION_FAILED.equals(state))
                throw new PaymentExecutionException(String.format("Payment Execution Failed for id %s", paymentData.getId()));

            TransactionType transaction = new TransactionType(UUID.randomUUID(), PaymentConstant.PAYMENT_EXECUTED, paymentData.getUserId(), paymentData.getAmount()
                .getTotal());
            record.setPaymentStatus(PaymentConstant.PAYMENT_EXECUTED);
            record.getTransactions()
                .add(transaction);
            savedRecord = recordRepository.save(record);

            Optional<PaymentByRequestId> requestOptional = requestRepository.findById(record.getIdempotencyKey());
            if (requestOptional.isPresent()) {
                PaymentByRequestId request = requestOptional.get();
                request.setPaymentStatus(PaymentConstant.PAYMENT_EXECUTED);
                requestRepository.save(request);
            }
        }

        broker.send(topic, utility.createPaymentEvent(paymentData, PaymentConstant.PAYMENT_EXECUTED));

        return Optional.ofNullable(savedRecord != null ? savedRecord.getId() : null);
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
