package com.baeldung.reactive.consumer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.producer.OrderProducer;
import com.baeldung.reactive.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderConsumer {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProducer orderProducer;

    @KafkaListener(topics = "orders", groupId = "orders")
    public void consume(Order order) throws IOException {
        log.info("Order received to process: {}", order);
        if ("INITIATION-SUCCESS".equals(order.getOrderStatus()))
            orderRepository.findById(order.getId())
                .map(o -> {
                    orderProducer.sendMessage(o.setOrderStatus("RESERVE-INVENTORY"));
                    return o.setOrderStatus(order.getOrderStatus())
                        .setResponseMessage(order.getResponseMessage());
                })
                .flatMap(orderRepository::save)
                .subscribe();
        else if ("INVENTORY-SUCCESS".equals(order.getOrderStatus()))
            orderRepository.findById(order.getId())
                .map(o -> {
                    orderProducer.sendMessage(o.setOrderStatus("PREPARE-SHIPPING"));
                    return o.setOrderStatus(order.getOrderStatus())
                        .setResponseMessage(order.getResponseMessage());
                })
                .flatMap(orderRepository::save)
                .subscribe();
        else if ("SHIPPING-FAILURE".equals(order.getOrderStatus()))
            orderRepository.findById(order.getId())
                .map(o -> {
                    orderProducer.sendMessage(o.setOrderStatus("REVERT-INVENTORY"));
                    return o.setOrderStatus(order.getOrderStatus())
                        .setResponseMessage(order.getResponseMessage());
                })
                .flatMap(orderRepository::save)
                .subscribe();
        else
            orderRepository.findById(order.getId())
                .map(o -> {
                    return o.setOrderStatus(order.getOrderStatus())
                        .setResponseMessage(order.getResponseMessage());
                })
                .flatMap(orderRepository::save)
                .subscribe();
    }
}