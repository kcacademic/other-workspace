package com.baeldung.reactive.controller;

import java.util.Arrays;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.reactive.domain.LineItem;
import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.domain.Product;
import com.baeldung.reactive.producer.OrderProducer;
import com.baeldung.reactive.service.ProductService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    OrderProducer orderProducer;

    @GetMapping
    public Flux<Product> getAllProducts() {
        log.info("Get all products invoked.");

        Order order = new Order();
        order.setId(new ObjectId());
        order.setUserId("" + Math.random());
        order.setLineItems(Arrays.asList(
            new LineItem().setProductId(new ObjectId("5edcbfd30717397ae8cfb7f0"))
            .setQuantity(1),
            new LineItem().setProductId(new ObjectId("5edcbfd30717397ae8cfb7f1"))
                .setQuantity(100)));
        orderProducer.sendMessage(order);

        return productService.getProducts();
    }

}
