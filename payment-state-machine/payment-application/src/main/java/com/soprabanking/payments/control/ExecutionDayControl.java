package com.soprabanking.payments.control;

import com.soprabanking.payments.domain.PaymentObject;
import com.soprabanking.payments.feedback.Data;
import com.soprabanking.payments.feedback.Feedback;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Date;

@Component
public class ExecutionDayControl implements Control<PaymentObject> {

    @Override
    public Flux<Feedback<PaymentObject>> validate(PaymentObject paymentObject) {

        return Flux.just(new Data<PaymentObject>(p -> p.setExecutionDate(new Date())));
    }

}
