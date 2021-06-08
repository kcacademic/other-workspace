package com.kchandrakant.learning

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

class Car(make: String, model: String) : Vehicle() {
    private val description = "($make, $model)"

    constructor(make: String) : this(make, "Unknown") {
        println(description)
    }

    override fun accelerate() {}
    override fun decelerate() {}
}


fun main() {

    val insured = Insured { println("Renewing") }
    insured.renew()

    val car = Car("Ford")
    car.accelerate()

    val bike = Bike()
    bike.start()

    val bus = Bus(BaseInsured())
    bus.accelerate()

    val x: String? = null
    println(x?.length) // Safe call, will not result into null pointer exception
    println(x!!.length) // Non-null asserted call, will result into null pointer exception

}