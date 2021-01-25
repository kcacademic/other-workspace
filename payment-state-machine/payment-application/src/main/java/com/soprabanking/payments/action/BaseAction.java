package com.soprabanking.payments.action;

import com.soprabanking.payments.domain.Message;
import com.soprabanking.payments.domain.PaymentObject;
import com.soprabanking.payments.domain.Transition;
import com.soprabanking.payments.factory.ValidationFactory;
import com.soprabanking.payments.producer.PaymentEventProducer;
import com.soprabanking.payments.strategy.ValidationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Date;

public abstract class BaseAction implements Action<PaymentObject> {

    @Autowired
    ValidationFactory validationFactory;

    @Autowired
    ValidationStrategy<PaymentObject> validationStrategy;

    @Autowired
    PaymentEventProducer paymentEventProducer;

    Mono<PaymentObject> validate(PaymentObject paymentObject, Transition transition) {
        return Mono.just(paymentObject)
                .flatMap(payment -> {
                    return validationFactory.<PaymentObject>getValidators(
                            PaymentObject.class.getName()
                                    + "-" + transition.getId())
                            .collectList()
                            .flatMap(controls -> validationStrategy.validate(payment, controls));
                });
    }

    Mono<PaymentObject> post(PaymentObject paymentObject, Transition<PaymentObject> transition) {

        return Mono.just(paymentObject).doOnNext(payment -> paymentEventProducer.generate(new Message(paymentObject, transition)));

    }

}
