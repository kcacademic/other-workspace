package com.kchandrakant.payments.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "payments.statemachine")
data class StateMachineConfig(

        var machines: List<Machine>

)

data class Machine(
        var id: String = "",
        var domainObject: String = "",
        var allStates: MutableList<String> = mutableListOf(),
        var allEvents: MutableList<String> = mutableListOf(),
        var rootState: String = "",
        var transitions: MutableList<Transition> = mutableListOf()
)

data class Transition(
        var id: String = "",
        var initialState: String = "",
        var finalState: MutableMap<String, String> = mutableMapOf(),
        var event: String = "",
        var actions: MutableList<String> = mutableListOf()
)