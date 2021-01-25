package com.soprabanking.payments.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "payments.validation")
data class ValidationConfig(
        var validators: List<Validator>
)

data class Validator(
        var domainObject: String = "",
        var stages: MutableList<Stage> = mutableListOf()
)

data class Stage(
        var transition: String = "",
        var controls: MutableList<String> = mutableListOf()
)