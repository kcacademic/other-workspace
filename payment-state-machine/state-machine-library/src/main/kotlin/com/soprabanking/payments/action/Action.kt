package com.soprabanking.payments.action

import com.soprabanking.payments.domain.BaseStateful
import com.soprabanking.payments.domain.Transition
import reactor.core.publisher.Mono

interface Action<T : BaseStateful> {

    fun perform(data: T, transition: Transition<T>): Mono<T>
}