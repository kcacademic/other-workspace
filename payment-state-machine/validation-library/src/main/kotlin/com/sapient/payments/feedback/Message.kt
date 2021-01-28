package com.sapient.payments.feedback

import com.sapient.payments.domain.BaseValidatable

data class Message<T : BaseValidatable>(override var function: (T) -> Unit, val messageType: MessageType) : Feedback<T>(function)

enum class MessageType { INFO, ERROR }