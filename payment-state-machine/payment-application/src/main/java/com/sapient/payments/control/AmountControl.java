package com.sapient.payments.control;

import com.sapient.payments.feedback.Feedback;
import com.sapient.payments.feedback.Message;
import com.sapient.payments.feedback.MessageType;
import com.sapient.payments.domain.PaymentObject;
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
