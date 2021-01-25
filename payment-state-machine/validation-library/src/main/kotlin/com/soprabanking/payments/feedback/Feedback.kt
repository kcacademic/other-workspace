package com.soprabanking.payments.feedback

import com.soprabanking.payments.domain.BaseValidatable
import java.util.function.Consumer

open class Feedback<T : BaseValidatable>(
        open var function: Consumer<T>
)