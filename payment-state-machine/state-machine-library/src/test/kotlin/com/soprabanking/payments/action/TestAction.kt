package com.soprabanking.payments.action

import com.soprabanking.payments.domain.TestDomain
import com.soprabanking.payments.domain.Transition
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class TestAction : Action<TestDomain> {

    override fun perform(data: TestDomain, transition: Transition<TestDomain>): Mono<TestDomain> {
        return Mono.just(data).doOnNext{ println("TestAction for: $data")}
    }

}