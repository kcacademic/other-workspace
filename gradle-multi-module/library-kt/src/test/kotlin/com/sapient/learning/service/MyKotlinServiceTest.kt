package com.sapient.learning.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest("service.message=Hello")
class MyServiceTest {
    @Autowired
    private val myKotlinService: MyKotlinService? = null

    //@Test
    //fun contextLoads() {
    //    Assertions.assertThat(myKotlinService!!.message()).isNotNull
    //}

    @SpringBootApplication
    internal class TestConfiguration
}