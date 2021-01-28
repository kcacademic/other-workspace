package com.sapient.payments.control

import com.sapient.payments.domain.TestDomain
import com.sapient.payments.feedback.Data
import com.sapient.payments.feedback.Feedback
import com.sapient.payments.feedback.Message
import com.sapient.payments.feedback.MessageType
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.util.function.Consumer

@Component
class TestControl : Control<TestDomain> {
    override fun validate(data: TestDomain): Flux<Feedback<TestDomain>> {

        return Flux.just(
                Data<TestDomain> { t -> t.testData = "Hello World!" },
                Message<TestDomain>(function = { }, messageType = MessageType.INFO)
        );
    }

}