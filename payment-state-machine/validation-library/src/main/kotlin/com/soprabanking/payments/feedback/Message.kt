package com.soprabanking.payments.feedback

import com.soprabanking.payments.domain.BaseValidatable
import java.util.function.Consumer

data class Message<T : BaseValidatable>(override var function: Consumer<T>, val messageType: MessageType) : Feedback<T>(function)

enum class MessageType { INFO, ERROR }