package com.sapient.payments.action;

import com.sapient.payments.domain.PaymentObject;
import com.sapient.payments.domain.Transition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ValidationAction extends BaseAction {

    @Override
    public Mono<PaymentObject> perform(PaymentObject paymentObject, Transition<PaymentObject> transition) {

        return Mono.just(paymentObject)
                .flatMap(payment -> validate(payment, transition));

    }

}
