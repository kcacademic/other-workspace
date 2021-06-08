package com.kchandrakant.learning.config

import com.kchandrakant.learning.handler.DefaultErrorHandler
import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import org.springframework.util.ErrorHandler
import javax.jms.ConnectionFactory

@Configuration
open class ActiveMqConfig {

    @Value("\${activemq.brokers.url}")
    private val brokerUrl: String? = null

    @Value("\${activemq.brokers.username}")
    private val username: String? = null

    @Value("\${activemq.brokers.password}")
    private val password: String? = null

    @Value("\${queue.payments}")
    private val destination: String? = null

    @Bean
    open fun jmsTemplate(connectionFactory: ConnectionFactory?, jacksonJmsMessageConverter: MessageConverter?): JmsTemplate? {
        val jmsTemplate = JmsTemplate()
        jmsTemplate.connectionFactory = connectionFactory
        jmsTemplate.messageConverter = jacksonJmsMessageConverter
        jmsTemplate.defaultDestinationName = destination
        return jmsTemplate
    }

    @Bean
    open fun connectionFactory(): ConnectionFactory? {
        System.out.println("Broker URL: $brokerUrl");
        val connectionFactory = ActiveMQConnectionFactory(brokerUrl)
        //connectionFactory.setUserName(username);
        //connectionFactory.setPassword(password);
        return connectionFactory
    }

    @Bean
    open fun jmsListenerContainerFactory(connectionFactory: ConnectionFactory?, defaultErrorHandler: ErrorHandler?, jacksonJmsMessageConverter: MessageConverter?): JmsListenerContainerFactory<*>? {
        val listenerContainerFactory = DefaultJmsListenerContainerFactory()
        listenerContainerFactory.setConnectionFactory(connectionFactory!!)
        listenerContainerFactory.setErrorHandler(defaultErrorHandler!!)
        listenerContainerFactory.setMessageConverter(jacksonJmsMessageConverter!!)
        return listenerContainerFactory
    }

    @Bean
    open fun jacksonJmsMessageConverter(): MessageConverter? {
        val converter = MappingJackson2MessageConverter()
        converter.setTargetType(MessageType.TEXT)
        converter.setTypeIdPropertyName("_type")
        return converter
    }

    @Bean
    open fun defaultErrorHandler(): ErrorHandler? {
        return DefaultErrorHandler()
    }
}