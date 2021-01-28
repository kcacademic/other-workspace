package com.sapient.payments.feedback

import com.sapient.payments.domain.BaseValidatable

open class Feedback<T : BaseValidatable>(
        open var function: (T) -> Unit
)