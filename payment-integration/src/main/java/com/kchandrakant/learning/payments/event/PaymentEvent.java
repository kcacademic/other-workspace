package com.kchandrakant.learning.payments.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentEvent {

    private String id;
    private String userId;
    private String paymentProvider;
    private String paymentMethod;
    private String eventType;
    private String amount;

}
