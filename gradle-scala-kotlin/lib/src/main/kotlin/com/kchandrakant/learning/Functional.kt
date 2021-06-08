package com.kchandrakant.learning

val myFunction = { x: Int -> x * 2 }

inline fun handle(number: Int, function: (Int) -> Int): Int = function(number)

fun weight(gravity: Double) = { mass: Double -> mass * gravity }
val weightOnEarth = weight(9.81)

fun fibonacci(x: Int): Int {
    fun next(x: Int, y: Int, current: Int, target: Int): Int {
        return if (current == target) y
        else next(y, x + y, current + 1, target)
    }
    return if (x == 1) 0 else if (x == 2) 1 else next(0, 1, 2, x)
}

data class Planet(val gravity: Double)

fun factorial(x: Int): Int {
    tailrec fun next(x: Int, result: Int): Int {
        return if (x == 1) result
        else next(x - 1, result * x)
    }
    return next(x, 1)
}

fun main() {

    println(myFunction(2))
    println(handle(4, myFunction))

    println(fibonacci(10))

    println(weight(9.81)(80.0))
    println(weightOnEarth(80.0))

    val earth = Planet(9.81)
    // earth.gravity = 10 // Illegal assignment

    println(factorial(5))

}