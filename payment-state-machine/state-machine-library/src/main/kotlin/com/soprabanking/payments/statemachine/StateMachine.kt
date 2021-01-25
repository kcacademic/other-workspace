package com.soprabanking.payments.statemachine

import com.soprabanking.payments.domain.BaseStateful
import com.soprabanking.payments.domain.Transition
import reactor.core.publisher.Mono

open class StateMachine<T : BaseStateful> {

    var id: String = ""
    var rootState: String = ""
    lateinit var allStates: List<String>
    lateinit var allEvents: List<String>
    lateinit var transitions: MutableList<Transition<T>>

    fun handle(data: T, event: String): Mono<T> {
        return Mono.just(transitions
                .filter { (_, initialState) -> initialState == data.state }
                .first { (_, _, _, event1) -> event1 == event })
                .flatMap { handleTransition(data, it!!) }
                .flatMap(::handleAutomaticTransition)
                .defaultIfEmpty(data)
    }

    private fun handleTransition(data: T, transition: Transition<T>): Mono<T> {

        return Mono.just(transition)
                .flatMap { tr: Transition<T> ->
                    val start: Mono<T>? = if (tr.action != null) tr.action?.perform(data, tr) else Mono.just(data)
                    start?.map {
                        it.state = tr.finalState["default"].toString()
                        it
                    }?.onErrorResume {
                        data.state = tr.finalState["error"].toString()
                        Mono.just(data)
                    }
                }
    }

    private fun handleAutomaticTransition(data: T): Mono<T> {

        val list = transitions
                .filter { tr: Transition<T> -> tr.initialState == data.state }
                .filter { tr: Transition<T> -> tr.event == "AUTOMATIC" }
        val start: Mono<Transition<T>> = if (list.isEmpty()) Mono.empty<Transition<T>>() else Mono.just(list[0])
        return start
                .flatMap { handleTransition(data, it) }
                .flatMap(::handleAutomaticTransition)

    }

}