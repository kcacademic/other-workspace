package com.sapient.learning

interface BaseInterface {
	
	val prop: Int // abstract
}

interface MyInterface : BaseInterface {

    val propertyWithImplementation: String
        get() = "foo"

	fun bar()
	
    fun foo() {
        print(prop)
    }
}