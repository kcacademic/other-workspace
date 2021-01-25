package com.soprabanking.payments.resource;

import com.soprabanking.payments.domain.Event;
import com.soprabanking.payments.domain.PaymentObject;
import com.soprabanking.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/initiate")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PaymentObject> initiate(@RequestBody PaymentObject paymentObject) {
        return paymentService.initiate(paymentObject);
    }

    @PostMapping("/process")
    public Mono<PaymentObject> process(@RequestBody Event event) {
        return paymentService.handle(event.getPaymentId(), event.getEventType());
    }

}
