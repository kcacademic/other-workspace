package com.kchandrakant.learning

class InvariantGarden<T>
class CovariantGarden<out T>
class ContravariantGarden<in T>

open class Fruit

class Apple : Fruit()

class Banana : Fruit()

fun main() {
    //var myInvariantGarden: InvariantGarden<Fruit> = InvariantGarden<Apple>() //This is illegal
    //var myInvariantGarden: InvariantGarden<Apple> = InvariantGarden<Fruit>() //This is illegal

    var myCovariantGarden: CovariantGarden<Fruit> = CovariantGarden<Apple>() //This is legal
    //var myCovariantGarden: CovariantGarden<Apple> = CovariantGarden<Fruit>() //This is illegal

    //var myContravariantGarden: ContravariantGarden<Fruit> = ContravariantGarden<Apple>() //This is legal
    var myContravariantGarden: ContravariantGarden<Apple> = ContravariantGarden<Fruit>() //This is illegal
}