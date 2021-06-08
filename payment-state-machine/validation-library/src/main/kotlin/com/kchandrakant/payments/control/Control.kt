package com.kchandrakant.payments.control

import com.sapient.payments.domain.BaseValidatable
import com.sapient.payments.feedback.Feedback
import reactor.core.publisher.Flux

interface Control<T : BaseValidatable> {

    fun validate(data: T): Flux<Feedback<T>>

}