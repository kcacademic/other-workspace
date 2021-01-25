package com.sapient.payments.action

import com.sapient.payments.domain.BaseStateful
import com.sapient.payments.domain.Transition
import reactor.core.publisher.Mono

interface Action<T : BaseStateful> {

    fun perform(data: T, transition: Transition<T>): Mono<T>
}