package com.soprabanking.payments

import com.soprabanking.payments.domain.MyDomain
import com.soprabanking.payments.domain.TestDomain
import com.soprabanking.payments.factory.StateMachineFactory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import reactor.test.StepVerifier

@SpringBootTest
class StateMachineUnitTest(@Autowired val stateMachineFactory: StateMachineFactory) {

    @Test
    fun test1() {
        StepVerifier.create(
                stateMachineFactory.getStateMachine(TestDomain::class.java)
                        .flatMap { it.handle(TestDomain("CREATED"), "VALIDATE") })
                .expectNext(TestDomain("DISPATCHED"))
                .verifyComplete()
    }

    @Test
    fun test2() {
        StepVerifier.create(
                stateMachineFactory.getStateMachine(MyDomain::class.java)
                        .flatMap { it.handle(MyDomain("CREATED"), "VALIDATE") })
                .expectNext(MyDomain("VERIFIED"))
                .verifyComplete()
    }

    @SpringBootApplication
    internal class TestConfiguration
}