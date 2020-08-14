package com.sapient.learning.listener;

import com.sapient.learning.domain.Payment;
import com.sapient.learning.utility.EmbeddedActiveMq;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@EmbeddedActiveMq
public class MyListenerTest {

    @BeforeAll
    public static void setUp() {
    }

    @Autowired
    JmsTemplate jmsTemplate;

    @Test
    public void messageSend() {
        Payment payment = new Payment(100L, "kumar--", "kumar");
        jmsTemplate.convertAndSend(payment);
    }

}
