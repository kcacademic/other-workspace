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
public class PaymentData {

    private String id;
    private String idempotencyKey;
    private String userId;
    private String paymentProvider;
    private String authUrl;
    private String state;
    private String paymentMethod;
    private String description;
    private String intent;
    private String payerId;
    private String token;

    private Amount amount;
    private Address address;
    private Card card;

}
