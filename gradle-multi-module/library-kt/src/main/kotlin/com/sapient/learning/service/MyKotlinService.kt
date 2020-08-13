package com.sapient.learning.service

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service


@Service
@EnableConfigurationProperties(ServiceProperties::class)
class MyKotlinService(private val serviceProperties: ServiceProperties) {
    fun message(): String? {
        return serviceProperties.message
    }

}