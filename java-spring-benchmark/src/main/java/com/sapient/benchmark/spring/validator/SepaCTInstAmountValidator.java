package com.sapient.benchmark.spring.validator;

import com.sapient.benchmark.spring.model.PaymentOrderApiDto;
import com.sapient.benchmark.spring.validator.base.PaymentOrderValidator;
import org.springframework.stereotype.Component;

@Component
public class SepaCTInstAmountValidator extends PaymentOrderValidator {

    @Override
    public PaymentOrderApiDto validate(PaymentOrderApiDto data) {
        return null;
    }
}
