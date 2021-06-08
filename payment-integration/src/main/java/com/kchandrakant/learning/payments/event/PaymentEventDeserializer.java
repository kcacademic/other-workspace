package com.kchandrakant.learning.payments.event;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PaymentEventDeserializer implements Deserializer<PaymentEvent> {

    Logger logger = LoggerFactory.getLogger(PaymentEventDeserializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

        logger.error("Method Not Supported");

    }

    @Override
    public PaymentEvent deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        PaymentEvent event = null;
        try {
            event = mapper.readValue(data, PaymentEvent.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return event;
    }

    @Override
    public void close() {

        logger.error("Method Not Supported");

    }

}
