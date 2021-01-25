package com.soprabanking.payments.service;

import com.soprabanking.payments.domain.PaymentObject;
import com.soprabanking.payments.factory.StateMachineFactory;
import com.soprabanking.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    StateMachineFactory stateMachineFactory;

    public Mono<PaymentObject> initiate(PaymentObject paymentObject) {
        return Mono.just(paymentObject)
                .flatMap(payment -> stateMachineFactory.getStateMachine(PaymentObject.class)
                        .map(stateMachine -> {
                            payment.setState(stateMachine.getRootState());
                            return payment;
                        }))
                .flatMap(payment -> paymentRepository.save(payment));
    }

    public Mono<PaymentObject> handle(String paymentId, String event) {
        return Mono.just(paymentId)
                .flatMap(id -> paymentRepository.get(id))
                .flatMap(payment -> stateMachineFactory.getStateMachine(PaymentObject.class)
                        .flatMap(stateMachine -> stateMachine.handle(payment, event)))
                .flatMap(payment -> paymentRepository.save(payment));
    }

}
