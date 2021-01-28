package com.sapient.payments.control;

import com.sapient.payments.feedback.Data;
import com.sapient.payments.feedback.Feedback;
import com.sapient.payments.domain.PaymentObject;
import kotlin.Unit;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Date;

@Component
public class ExecutionDayControl implements Control<PaymentObject> {

    @Override
    public Flux<Feedback<PaymentObject>> validate(PaymentObject paymentObject) {

        return Flux.just(new Data<PaymentObject>(p -> {
            p.setExecutionDate(new Date());
            return Unit.INSTANCE;
        }));
    }

}
