package com.soprabanking.payments.control

import com.soprabanking.payments.domain.BaseValidatable
import com.soprabanking.payments.feedback.Feedback
import reactor.core.publisher.Flux

interface Control<T : BaseValidatable> {

    fun validate(data: T): Flux<Feedback<T>>

}