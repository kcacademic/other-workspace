package com.soprabanking.payments.control

import com.soprabanking.payments.domain.TestDomain
import com.soprabanking.payments.feedback.Data
import com.soprabanking.payments.feedback.Feedback
import com.soprabanking.payments.feedback.Message
import com.soprabanking.payments.feedback.MessageType
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Component
class TestControl : Control<TestDomain> {
    override fun validate(data: TestDomain): Flux<Feedback<TestDomain>> {

        return Flux.just(
                Data<TestDomain>(function = Consumer { it.testData = "Hello World!" }),
                Message<TestDomain>(function = Consumer { }, messageType = MessageType.INFO)
        );
    }

}