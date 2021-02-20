package com.sapient.learning

object Others {

  def findTaxi(model: String): Option[Taxi] = {
    val taxis = Map("Ford" -> new Taxi("Ford"))
    taxis.get(model)
  }

  def dayOfWeek(x: Int): String = x match {
    case 1 => "Monday"
    case 2 => "Tuesday"
    case _ => "other"
  }

  abstract class Order
  case class MailOrder(name: String, address: String) extends Order
  case class WebOrder(name: String, email: String, phone: String) extends Order

  def generateResponse(order: Order): String = {
    order match {
      case MailOrder(name, address) => s"Thank you $name, you will receive confirmation through post at: $address"
      case WebOrder(name, email, _) => s"Thank you $name, you will receive confirmation through email at: $email"
    }
  }


  def main(args: Array[String]): Unit = {

    println(findTaxi("Ford"))
    println(findTaxi("Honda"))

    println(dayOfWeek(1))
    println(dayOfWeek(10))

  }

}
