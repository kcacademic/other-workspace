package com.sapient.learning

abstract class Vehicle {
  def accelerate(): Unit

  def decelerate(): Unit
}

trait Electric {
  this: Insured =>
  def recharge(): Unit
}

trait Insured {
  def renew(): Unit
}

class Taxi(val make: String, val model: String) extends Vehicle with Electric with Insured {

  def this(make: String) = {
    this(make, "Unknown")
  }

  private val _description = s"($make, $model)"

  {
    import Taxi._
    log(_description)
  }

  def description: String = _description

  def accelerate(): Unit = println("Accelerating")

  def decelerate(): Unit = println("decelerating")

  def recharge(): Unit = println("Recharging")

  def renew(): Unit = println("Renewing")

}

object Taxi {
  private def log(message: String): Unit = Logger.info(message)
}

object Logger {
  def info(message: String): Unit = println(s"INFO: $message")
}

object ObjectOriented {

  def show(implicit message: String): Unit = {
    println(message)
  }

  def main(args: Array[String]): Unit = {
    val myTaxi = new Taxi("Ford", "1986")
    Logger.info(myTaxi.description)
    myTaxi.accelerate()
    myTaxi.decelerate()
    myTaxi.recharge()
    myTaxi.renew()

    implicit val message:String = "Hello World!"
    show
    show("Hi World!")

  }
}
