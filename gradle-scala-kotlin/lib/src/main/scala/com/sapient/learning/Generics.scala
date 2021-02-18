package com.sapient.learning

class InvariantGarden[A]

class CovariantGarden[+A]

class ContravariantGarden[-A]

class Fruit

class Banana extends Fruit

class Apple extends Fruit

class Generics {

  def main(args: Array[String]): Unit = {
    //val myInvariantGarden: InvariantGarden[Fruit] = new InvariantGarden[Banana] // Illegal statement
    //val myInvariantGarden: InvariantGarden[Banana] = new InvariantGarden[Fruit] // Illegal statement

    val myCovariantGarden: CovariantGarden[Fruit] = new CovariantGarden[Banana]
    //val myCovariantGarden: CovariantGarden[Banana] = new CovariantGarden[Fruit] // Illegal statement

    //val myContravariantGarden: ContravariantGarden[Fruit] = new ContravariantGarden[Banana] // Illegal statement
    val myContravariantGarden: ContravariantGarden[Banana] = new ContravariantGarden[Fruit]
  }

}
