package com.coc.payments.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.coc.payments.vo.PaymentCard;

@Component
public class CardValidator {

    private CardValidator() {

    }

    public static void validate(PaymentCard card, Errors errors) {

        if (card == null)
            errors.reject("Card data can not be null for card payments.");

        else if (card.getCardId() == null) {
            if (card.getNumber() == null || card.getExpirationYear() == null || card.getExpirationMonth() == null)
                errors.reject("Card Number, Expiration Year and Month are mandatory for card payments.");

            if (card.getSecurityCode() == null)
                errors.reject("Card security number is mandatory for card payments.");
        }
    }
}
