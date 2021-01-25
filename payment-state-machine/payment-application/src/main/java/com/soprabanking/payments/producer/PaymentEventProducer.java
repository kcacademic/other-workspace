package com.soprabanking.payments.producer;

import com.soprabanking.payments.domain.Message;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventProducer {

    public void generate(Message message) {
        System.out.println("EVENT: " + message);
    }

}
