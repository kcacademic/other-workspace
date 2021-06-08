package com.kchandrakant.learning.payments.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Card {

    private String cardId;
    private String userId;
    private String number;
    private String expirationYear;
    private String expirationMonth;
    private String securityCode;
    private boolean saveCard;

}
