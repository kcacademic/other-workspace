package com.coc.payments.exception;

public class PaymentRecordMissingException extends Exception {

    private static final long serialVersionUID = 1L;

    public PaymentRecordMissingException(String message) {
        super(message);
    }

}
