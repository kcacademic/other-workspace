package com.kchandrakant.learning.spring.validator;

import org.springframework.stereotype.Component;

import com.kchandrakant.learning.spring.model.PaymentConfirmationApiDto;
import com.kchandrakant.learning.spring.validator.base.PaymentConfirmationValidator;

@Component
public class StatusReportEnricher extends PaymentConfirmationValidator {

    @Override
    public PaymentConfirmationApiDto validate(PaymentConfirmationApiDto data) {
        return null;
    }
}
