package com.kchandrakant.learning.resource;

import com.kchandrakant.learning.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.jms.core.JmsTemplate;

@RestController
@RequestMapping("/api")
public class MyResource {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${queue.payments}")
    private String destination;

    @Value("${activemq.brokers.url}")
    private String brokerUrl;

    @PostMapping("/message")
    public ResponseEntity<Payment> send(@RequestBody Payment payment) {
        System.out.println("Brokers: " + brokerUrl);
        payment.initialize();
        jmsTemplate.convertAndSend(destination, payment);
        return ResponseEntity.ok(payment);
    }
}
