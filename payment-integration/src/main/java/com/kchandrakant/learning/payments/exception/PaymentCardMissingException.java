package com.kchandrakant.learning.payments.exception;

public class PaymentCardMissingException extends Exception {

    private static final long serialVersionUID = 1L;

    public PaymentCardMissingException(String message) {
        super(message);
    }

}
