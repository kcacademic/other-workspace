package com.soprabanking.payments.control;

import com.soprabanking.payments.domain.PaymentObject;
import com.soprabanking.payments.feedback.Feedback;
import com.soprabanking.payments.feedback.Message;
import com.soprabanking.payments.feedback.MessageType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AmountControl implements Control<PaymentObject> {

    @Override
    public Flux<Feedback<PaymentObject>> validate(PaymentObject paymentObject) {

        return Mono.just(paymentObject)
                .filter(payment -> payment.getAmount() == 0.0)
                .flatMapMany(payment -> Flux.just(new Message<PaymentObject>(p -> p.getErrors().add(ErrorCode.AMOUNT_NILL), MessageType.ERROR)));

    }

}
