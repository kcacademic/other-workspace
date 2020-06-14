package com.baeldung.reactive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.domain.Product;
import com.baeldung.reactive.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public Flux<Product> getAllProducts() {
        return productService.getProducts();
    }

    @PostMapping
    public Mono<Order> processOrder(@RequestBody Order order) {
        System.out.println("Process Order: " + order);
        return Mono.just(order)
            .filter(o -> o.getLineItems() != null)
            .flatMap(productService::handleOrder)
            .switchIfEmpty(Mono.just(order.setOrderStatus("FAILURE")));
    }

    @DeleteMapping
    public Mono<Order> revertOrder(@RequestBody Order order) {
        System.out.println("Revert Order: " + order);
        return Mono.just(order)
            .filter(o -> o.getLineItems() != null)
            .flatMap(productService::revertOrder)
            .switchIfEmpty(Mono.just(order.setOrderStatus("FAILURE")));
    }
}
