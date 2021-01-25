package com.sapient.payments.strategy

import com.sapient.payments.control.Control
import com.sapient.payments.domain.BaseValidatable
import reactor.core.publisher.Mono

interface ValidationStrategy<T : BaseValidatable> {

    fun validate(validatable: T, controls: List<Control<T>>): Mono<T>

}