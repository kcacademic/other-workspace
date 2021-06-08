package com.kchandrakant.learning

fun dayOfWeek(x: Int): String {
    return when (x) {
        1 -> "Monday"
        2 -> "Tuesday"
        else -> "other"
    }
}

fun main() {

    println(dayOfWeek(1))
    println(dayOfWeek(10))

}

fun divide(x: Double, y: Double): Double {
    if (y == 0.0) throw IllegalArgumentException("Illegal argument")
    return x / y
}

fun performDivision(x: Double, y: Double) {
    try {
        divide(x, y)
    } catch (ex: IllegalArgumentException) {
        println(ex.message)
    } finally {
        println("Performing cleanup")
    }
}