package com.baeldung.reactive.consumer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderConsumer {

    @Autowired
    ProductService productService;

    @KafkaListener(topics = "orders", groupId = "group_id")
    public void consume(Order order) throws IOException {
        log.info(String.format("#### -> Consumed message -> %s", order));
        productService.handleOrder(order)
            .doOnSuccess(o -> log.info("Order processed succesfully."))
            .doOnError(e -> log.error("Order failed to process: " + e))
            .subscribe();
    }
}