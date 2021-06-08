package com.kchandrakant.learning.payments.service;

import java.util.Optional;

import com.kchandrakant.learning.payments.entity.PaymentData;
import com.kchandrakant.learning.payments.exception.PaymentCardMissingException;
import com.kchandrakant.learning.payments.exception.PaymentCreationException;
import com.kchandrakant.learning.payments.exception.PaymentExecutionException;
import com.kchandrakant.learning.payments.exception.PaymentRecordMissingException;

public interface PaymentService {

    public Optional<String> createPayment(PaymentData payment) throws PaymentCreationException;

    public Optional<String> authenticatePayment(PaymentData paymentData) throws PaymentRecordMissingException;

    public Optional<String> executePayment(PaymentData paymentData) throws PaymentRecordMissingException, PaymentExecutionException, PaymentCardMissingException;

    public Optional<String> capturePayment(PaymentData paymentData) throws PaymentRecordMissingException;

    public Optional<PaymentData> fetchPayment(PaymentData paymentData) throws PaymentRecordMissingException;

}
