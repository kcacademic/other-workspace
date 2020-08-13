package com.sapient.learning.listener;

import com.sapient.learning.domain.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MyListener {

    private static Logger log = LoggerFactory.getLogger(MyListener.class);

    @JmsListener(destination = "${queue.payments}")
    public void receiveMessage(@Payload Payment payment) {
        log.info("Received:  {}", payment);
    }

}