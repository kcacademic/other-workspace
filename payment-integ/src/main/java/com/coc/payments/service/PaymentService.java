package com.coc.payments.service;

import java.util.Optional;

import com.coc.payments.entity.PaymentData;
import com.coc.payments.exception.PaymentCardMissingException;
import com.coc.payments.exception.PaymentCreationException;
import com.coc.payments.exception.PaymentExecutionException;
import com.coc.payments.exception.PaymentRecordMissingException;

public interface PaymentService {

    public Optional<String> createPayment(PaymentData payment) throws PaymentCreationException;

    public Optional<String> authenticatePayment(PaymentData paymentData) throws PaymentRecordMissingException;

    public Optional<String> executePayment(PaymentData paymentData) throws PaymentRecordMissingException, PaymentExecutionException, PaymentCardMissingException;

    public Optional<String> capturePayment(PaymentData paymentData) throws PaymentRecordMissingException;

    public Optional<PaymentData> fetchPayment(PaymentData paymentData) throws PaymentRecordMissingException;

}
