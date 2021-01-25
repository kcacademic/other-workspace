package com.sapient.payments.domain

open class TestDomain() : BaseValidatable {

    var testData: String = ""

    constructor(testData: String) : this() {
        this.testData = testData
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestDomain

        if (testData != other.testData) return false

        return true
    }

    override fun hashCode(): Int {
        return testData.hashCode()
    }

    override fun toString(): String {
        return "TestDomain(testData='$testData')"
    }

}
