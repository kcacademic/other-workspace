package com.baeldung.reactive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.reactive.constants.OrderStatus;
import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order create(@RequestBody Order order) {
        log.info("Create order invoked with: {}", order);
        Order processedOrder = orderService.createOrder(order);
        if (OrderStatus.FAILURE.equals(processedOrder.getOrderStatus()))
            throw new RuntimeException("Order processing failed, please try again later.");
        return processedOrder;
    }

    @GetMapping
    public List<Order> getAll() {
        log.info("Get all orders invoked.");
        return orderService.getOrders();
    }
}
