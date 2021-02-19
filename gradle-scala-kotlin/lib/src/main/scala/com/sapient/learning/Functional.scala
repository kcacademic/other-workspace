package com.sapient.learning

object Functional {

  def handle(number: Int, function: Int => Int): Int = function(number)

  val function: Int => Int = (x: Int) => x * 2

  def fibonacci(x: Int): Int = {
    @scala.annotation.tailrec
    def next(x: Int, y: Int, current: Int, target: Int): Int = {
      if (current == target) y
      else next(y, x + y, current + 1, target)
    }

    if (x == 1) 0 else if (x == 2) 1 else next(0, 1, 2, x)
  }

  def weight(gravity: Double): Double => Double = (mass: Double) => mass * gravity

  val weightOnEarth: Double => Double = weight(9.81)

  case class Planet(gravity: Double)

  val earth: Planet = Planet(9.81)
  //earth.gravity = 10 //illegal statement

  val rank: (String, Int) = ("John Doe", 25)


  import scala.annotation.tailrec

  def factorial(x: Int): Int = {
    @tailrec
    def next(x: Int, result: Int): Int = {
      if (x == 1) result
      else next(x - 1, result * x)
    }

    next(x, 1)
  }


  def main(args: Array[String]): Unit = {

    println(handle(4, function))

    println(fibonacci(25))

    println(weight(9.81)(80.0))
    println(weightOnEarth(80.0))

    println(rank)

    println(factorial(5))

  }

}
