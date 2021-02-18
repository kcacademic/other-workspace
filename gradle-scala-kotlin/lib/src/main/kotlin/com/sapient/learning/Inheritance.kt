package com.sapient.learning

abstract class Vehicle {
    abstract fun accelerate()
    abstract fun decelerate()
}

interface Electric {
    fun recharge()
}

fun interface Insured {
    fun renew()
}

class BaseInsured : Insured {
    override fun renew() {
        Logger.info("Renewing")
    }
}

class Bus(insured: Insured) : Vehicle(), Insured by insured {
    override fun accelerate() {
        Logger.info("Accelerating")
    }

    override fun decelerate() {
        Logger.info("Decelerating")
    }
}

val insured = Insured({ println("Renewing") })

object Logger {
    fun info(message: String) {
        println("INFO: $message")
    }
}

class Bike : Vehicle(), Electric, Insured {
    override fun accelerate() {
        log("Accelerating")
    }

    override fun decelerate() {
        log("Decelerating")
    }

    override fun recharge() {
        log("Recharge")
    }

    override fun renew() {
        log("Renew")
    }

    companion object {
        private fun log(message: String) {
            Logger.info(message)
        }
    }
}

fun Bike.start() {
    Logger.info("Starting")
}

class Car(val make: String, val model: String) : Vehicle() {
    private val description = "($make, $model)"

    constructor(make: String) : this(make, "Unknown") {
        print(description)
    }

    override fun accelerate() {}
    override fun decelerate() {}
}


fun main() {

    val car: Car = Car("Ford")

    val bike: Bike = Bike()
    bike.start()

    val bus: Bus = Bus(BaseInsured())

}