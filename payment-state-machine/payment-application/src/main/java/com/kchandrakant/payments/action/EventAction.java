package com.kchandrakant.payments.action;

import com.kchandrakant.payments.domain.PaymentObject;
import com.sapient.payments.domain.Transition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EventAction extends BaseAction {

    @Override
    public Mono<PaymentObject> perform(PaymentObject paymentObject, Transition<PaymentObject> transition) {

        return Mono.just(paymentObject)
                .flatMap(payment -> post(payment, transition));

    }

}
