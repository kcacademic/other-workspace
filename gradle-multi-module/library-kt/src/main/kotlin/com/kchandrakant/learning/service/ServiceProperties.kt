package com.kchandrakant.learning.service

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("service")
open class ServiceProperties {
    /**
     * A message for the service.
     */
    var message: String? = null

}
