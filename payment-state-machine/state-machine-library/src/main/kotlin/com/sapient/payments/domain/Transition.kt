package com.sapient.payments.domain

import com.sapient.payments.action.Action

data class Transition <T: BaseStateful>(
        var id: String,
        var initialState: String,
        var finalState: Map<String, String>,
        var event: String,
        var action: Action<T>?
)