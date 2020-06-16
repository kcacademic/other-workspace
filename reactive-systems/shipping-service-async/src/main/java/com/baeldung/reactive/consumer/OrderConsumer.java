package com.baeldung.reactive.consumer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.producer.OrderProducer;
import com.baeldung.reactive.service.ShippingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderConsumer {

    @Autowired
    ShippingService shippingService;

    @Autowired
    OrderProducer orderProducer;

    @KafkaListener(topics = "orders", groupId = "shipping")
    public void consume(Order order) throws IOException {
        log.info("Order received to process: {}", order);
        if ("PREPARE-SHIPPING".equals(order.getOrderStatus()))
            shippingService.handleOrder(order)
                .doOnSuccess(o -> {
                    log.info("Order processed succesfully.");
                    orderProducer.sendMessage(order.setOrderStatus("SHIPPING-SUCCESS")
                        .setShippingDate(o.getShippingDate()));
                })
                .doOnError(e -> {
                    log.error("Order failed to process: " + e);
                    orderProducer.sendMessage(order.setOrderStatus("SHIPPING-FAILURE")
                        .setResponseMessage(e.getMessage()));
                })
                .subscribe();
    }
}