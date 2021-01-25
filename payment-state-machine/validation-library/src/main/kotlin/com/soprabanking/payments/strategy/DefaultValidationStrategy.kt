package com.soprabanking.payments.strategy

import com.soprabanking.payments.control.Control
import com.soprabanking.payments.domain.BaseValidatable
import com.soprabanking.payments.feedback.Data
import com.soprabanking.payments.feedback.Message
import com.soprabanking.payments.feedback.MessageType
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class DefaultValidationStrategy<T : BaseValidatable> : ValidationStrategy<T> {

    override fun validate(validatable: T, controls: List<Control<T>>): Mono<T> {
        return Flux.fromIterable(controls)
                .flatMap { it.validate(validatable) }
                .collectList()
                .doOnNext {
                    val messageList: List<Message<T>> = it
                            .filter { f -> Message::class.java.isInstance(f) }
                            .map { f -> f as Message<T> }
                    messageList.forEach { m -> m.function.accept(validatable) }
                    println(messageList)
                    messageList.filter { m -> m.messageType == MessageType.ERROR }
                            .firstOrNull { throw RuntimeException("Errors occurred in validation: $messageList") }
                }
                .doOnNext {
                    it.filter { f -> Data::class.java.isInstance(f) }
                            .forEach { d -> d.function.accept(validatable) }
                }
                .then(Mono.just(validatable))
    }

}