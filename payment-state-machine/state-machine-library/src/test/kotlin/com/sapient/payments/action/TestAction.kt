package com.sapient.payments.action

import com.sapient.payments.domain.TestDomain
import com.sapient.payments.domain.Transition
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TestAction : Action<TestDomain> {

    override fun perform(data: TestDomain, transition: Transition<TestDomain>): Mono<TestDomain> {
        return Mono.just(data).doOnNext{ println("TestAction for: $data")}
    }

}