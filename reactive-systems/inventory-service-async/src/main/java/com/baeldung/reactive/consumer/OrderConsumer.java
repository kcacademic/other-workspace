package com.baeldung.reactive.consumer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.producer.OrderProducer;
import com.baeldung.reactive.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderConsumer {

    @Autowired
    ProductService productService;

    @Autowired
    OrderProducer orderProducer;

    @KafkaListener(topics = "orders", groupId = "inventory")
    public void consume(Order order) throws IOException {
        log.info("Order received to process: {}", order);
        if ("RESERVE-INVENTORY".equals(order.getOrderStatus()))
            productService.handleOrder(order)
                .doOnSuccess(o -> {
                    log.info("Order processed succesfully.");
                    orderProducer.sendMessage(order.setOrderStatus("INVENTORY-SUCCESS"));
                })
                .doOnError(e -> {
                    log.error("Order failed to process: " + e);
                    orderProducer.sendMessage(order.setOrderStatus("INVENTORY-FAILURE")
                        .setResponseMessage(e.getMessage()));
                })
                .subscribe();
        else if ("REVERT-INVENTORY".equals(order.getOrderStatus()))
            productService.revertOrder(order)
                .doOnSuccess(o -> {
                    log.info("Order reverted succesfully.");
                    orderProducer.sendMessage(order.setOrderStatus("INVENTORY-REVERT-SUCCESS"));
                })
                .doOnError(e -> {
                    log.error("Order failed to revert: " + e);
                    orderProducer.sendMessage(order.setOrderStatus("INVENTORY-REVERT-FAILURE")
                        .setResponseMessage(e.getMessage()));
                })
                .subscribe();
    }
}