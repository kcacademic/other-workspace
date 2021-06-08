package com.kchandrakant.payments.resource;

import com.kchandrakant.payments.domain.Event;
import com.kchandrakant.payments.domain.Event;
import com.kchandrakant.payments.domain.PaymentObject;
import com.kchandrakant.payments.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.CoreMatchers.*;

@SpringBootTest
@AutoConfigureWebTestClient
public class PaymentControllerUnitTest {

    private WebTestClient webTestClient;

    private PaymentRepository paymentRepository;

    public PaymentControllerUnitTest(@Autowired WebTestClient webTestClient, @Autowired PaymentRepository paymentRepository) {
        this.webTestClient = webTestClient;
        this.paymentRepository = paymentRepository;
    }

    @Test
    public void testInitiate() {

        webTestClient.post()
                .uri("/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new PaymentObject()), PaymentObject.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PaymentObject.class)
                .value(PaymentObject::getPaymentId, notNullValue());

    }

    @Test
    public void testProcess() {

        PaymentObject paymentObject = new PaymentObject();
        paymentObject.setAmount(10.0);
        paymentObject.setState("CREATED");
        String paymentId = paymentObject.getPaymentId();
        paymentRepository.save(paymentObject).subscribe();

        webTestClient.post()
                .uri("/process")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Event(paymentId, "VALIDATE")), Event.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentObject.class)
                .value(PaymentObject::getExecutionDate, notNullValue());

    }

}
