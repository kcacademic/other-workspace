package com.sapient.payments.domain

open class BaseStateful(
        var state: String
) {
    constructor() : this("")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseStateful

        if (state != other.state) return false

        return true
    }

    override fun hashCode(): Int {
        return state.hashCode()
    }

    override fun toString(): String {
        return "BaseStateful(state='$state')"
    }
}