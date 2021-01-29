package com.sapient.payments.statemachine

import com.sapient.payments.domain.BaseStateful
import com.sapient.payments.domain.Transition
import reactor.core.publisher.Flux
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

        return Flux.fromIterable(transition.actions)
                .flatMap { it.perform(data, transition) }
                .collectList()
                .map {
                    data.state = transition.finalState["default"].toString()
                    data
                }
                .onErrorResume {
                    data.state = transition.finalState["error"].toString()
                    Mono.just(data)
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