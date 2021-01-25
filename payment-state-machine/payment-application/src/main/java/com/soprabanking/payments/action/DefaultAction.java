package com.soprabanking.payments.action;

import com.soprabanking.payments.domain.PaymentObject;
import com.soprabanking.payments.domain.Transition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DefaultAction extends BaseAction {

    @Override
    public Mono<PaymentObject> perform(PaymentObject paymentObject, Transition<PaymentObject> transition) {

        return Mono.just(paymentObject)
                .flatMap(payment -> post(payment, transition))
                .flatMap(payment -> validate(payment, transition));

    }

}
