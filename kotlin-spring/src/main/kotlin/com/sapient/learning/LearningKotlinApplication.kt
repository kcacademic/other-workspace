package com.sapient.learning

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class LearningKotlinApplication

fun main(args: Array<String>) {
    runApplication<LearningKotlinApplication>(*args)
}
