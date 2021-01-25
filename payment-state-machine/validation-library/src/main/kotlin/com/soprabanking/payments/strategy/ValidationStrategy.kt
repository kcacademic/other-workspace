package com.soprabanking.payments.strategy

import com.soprabanking.payments.control.Control
import com.soprabanking.payments.domain.BaseValidatable
import reactor.core.publisher.Mono

interface ValidationStrategy<T : BaseValidatable> {

    fun validate(validatable: T, controls: List<Control<T>>): Mono<T>

}