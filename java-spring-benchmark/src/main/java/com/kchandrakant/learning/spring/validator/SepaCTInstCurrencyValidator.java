package com.kchandrakant.learning.spring.validator;

import org.springframework.stereotype.Component;

import com.kchandrakant.learning.spring.model.PaymentOrderApiDto;
import com.kchandrakant.learning.spring.validator.base.PaymentOrderValidator;

@Component
public class SepaCTInstCurrencyValidator extends PaymentOrderValidator {

    @Override
    public PaymentOrderApiDto validate(PaymentOrderApiDto data) {
        return null;
    }
}
