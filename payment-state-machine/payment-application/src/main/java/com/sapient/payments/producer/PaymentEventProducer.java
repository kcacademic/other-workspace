package com.sapient.payments.producer;

import com.sapient.payments.domain.Message;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventProducer {

    public void generate(Message message) {
        System.out.println("EVENT: " + message);
    }

}
