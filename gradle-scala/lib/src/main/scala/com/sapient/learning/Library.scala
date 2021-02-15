package com.sapient.learning

class Garage[T <: Vehicle]

class Vehicle

class Car extends Vehicle

class Bike extends Vehicle

object Main {
  def main() {
    val myGarage: Garage[Vehicle] = new Garage[Vehicle]
    //val myGarage: Garage[Vehicle] = new Garage[Car]
    //val myGarage: Garage[Car] = new Garage[Vehicle]
  }
}
