package com.kchandrakant.payments.feedback

import com.sapient.payments.domain.BaseValidatable

data class Data<T : BaseValidatable>(override var function: (T) -> Unit) : Feedback<T>(function)