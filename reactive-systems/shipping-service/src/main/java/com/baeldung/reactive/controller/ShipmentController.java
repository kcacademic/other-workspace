package com.baeldung.reactive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.service.ShippingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    ShippingService shippingService;

    @PostMapping
    public Order process(@RequestBody Order order) {
        log.info("Process order invoked with: {}", order);
        return shippingService.handleOrder(order);
    }
}
