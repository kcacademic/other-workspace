package com.sapient.benchmark.spring.validator;

import com.sapient.benchmark.spring.model.PaymentConfirmationApiDto;
import com.sapient.benchmark.spring.validator.base.PaymentConfirmationValidator;
import org.springframework.stereotype.Component;

@Component
public class CancelReservationValidator extends PaymentConfirmationValidator {

    @Override
    public PaymentConfirmationApiDto validate(PaymentConfirmationApiDto data) {
        return null;
    }
}
