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

class Car(var make: String, var model: String) extends Vehicle with Electric with Insured {

  def this(make: String) = {
    this(make, "Unknown")
  }

  private val _description = s"($make, $model)"

  {
    import Car._
    log(_description)
  }

  def description: String = _description

  def accelerate(): Unit = println("Accelerating")

  def decelerate(): Unit = println("decelerating")

  def recharge(): Unit = println("Recharging")

  def renew(): Unit = println("Renewing")

}

object Car {
  private def log(message: String): Unit = Logger.info(message)
}

object Logger {
  def info(message: String): Unit = println(s"INFO: $message")
}

object ObjectOriented {
  def main(args: Array[String]): Unit = {
    val myCar = new Car("Ford", "1986")
    Logger.info(myCar.description)
    myCar.accelerate()
    myCar.decelerate()
    myCar.recharge()
    myCar.renew()
  }
}
