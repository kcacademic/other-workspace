package com.sapient.payments.repository;

import com.sapient.payments.domain.PaymentObject;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentRepository {

    Map<String, PaymentObject> store = new HashMap<>();

    public Mono<PaymentObject> save(PaymentObject paymentObject) {
        return Mono.just(paymentObject)
                .doOnNext(payment -> store.put(payment.getPaymentId(), payment));
    }

    public Mono<PaymentObject> get(String paymentId) {
        return Mono.just(paymentId)
                .handle((id, sink) -> {
                    if (store.containsKey(id))
                        sink.next(id);
                    else
                        sink.error(new RuntimeException("The payment could not be located: " + id));
                })
                .map(id -> store.get(id));
    }

}
