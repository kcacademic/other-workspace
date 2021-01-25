package com.sapient.payments.feedback

import com.sapient.payments.domain.BaseValidatable
import java.util.function.Consumer

data class Data<T : BaseValidatable>(override var function: Consumer<T>) : Feedback<T>(function)