package com.sapient.learning

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