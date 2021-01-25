package com.sapient.payments.factory

import com.sapient.payments.action.Action
import com.sapient.payments.config.StateMachineConfig
import com.sapient.payments.domain.BaseStateful
import com.sapient.payments.domain.Transition
import com.sapient.payments.statemachine.StateMachine
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import javax.annotation.PostConstruct


@Component
class StateMachineFactory(val stateMachineConfig: StateMachineConfig, val applicationContext: AbstractApplicationContext) {

    val stateMachines: MutableMap<String, StateMachine<*>> = mutableMapOf()

    @PostConstruct
    fun <T : BaseStateful> prepare() {
        println(stateMachineConfig.machines)
        for (machine in stateMachineConfig.machines) {
            val stateMachine: StateMachine<T> = StateMachine()
            val transitions: MutableList<Transition<T>> = mutableListOf()
            for (transition in machine.transitions) {
                val action: Action<T>? =
                        if (transition.action.isNotEmpty())
                            applicationContext.getBean(transition.action) as Action<T> else null// Handle case where action is null
                transitions.add(Transition(
                        transition.id,
                        transition.initialState,
                        transition.finalState,
                        transition.event,
                        action))
            }
            stateMachine.transitions = transitions
            stateMachine.rootState = machine.rootState
            stateMachine.allStates = machine.allStates
            stateMachine.allEvents = machine.allEvents
            stateMachines[machine.domainObject + "-" + machine.id] = stateMachine
        }
    }

    fun <T : BaseStateful> getStateMachine(type: Class<T>): Mono<StateMachine<T>> {
        return Mono.just(type)
                .map {
                    if (stateMachines.containsKey(it.name + "-default")) {
                        return@map stateMachines[it.name + "-default"] as StateMachine<T>?
                    } else {
                        throw RuntimeException("The State Machine could not be located for the type: " + it.name)
                    }
                }
    }


}