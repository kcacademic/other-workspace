package com.kchandrakant.learning.payments.event;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentEventSerializer implements Serializer<PaymentEvent> {

    Logger logger = LoggerFactory.getLogger(PaymentEventSerializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

        logger.error("Method Not Supported");

    }

    @Override
    public byte[] serialize(String topic, PaymentEvent data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(data)
                .getBytes();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return retVal;
    }

    @Override
    public void close() {

        logger.error("Method Not Supported");

    }

}
