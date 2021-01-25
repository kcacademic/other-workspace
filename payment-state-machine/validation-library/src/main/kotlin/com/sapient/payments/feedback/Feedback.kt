package com.sapient.payments.feedback

import com.sapient.payments.domain.BaseValidatable
import java.util.function.Consumer

open class Feedback<T : BaseValidatable>(
        open var function: Consumer<T>
)