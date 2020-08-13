package com.sapient.learning.handler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.ErrorHandler

class DefaultErrorHandler : ErrorHandler {

    override fun handleError(t: Throwable) {
        log.error(t.message)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ErrorHandler::class.java)
    }
}