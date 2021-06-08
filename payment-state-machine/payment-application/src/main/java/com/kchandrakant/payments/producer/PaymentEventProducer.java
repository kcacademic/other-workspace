package com.kchandrakant.payments.producer;

import com.kchandrakant.payments.domain.Message;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventProducer {

    public void generate(Message message) {
        System.out.println("EVENT: " + message);
    }

}
