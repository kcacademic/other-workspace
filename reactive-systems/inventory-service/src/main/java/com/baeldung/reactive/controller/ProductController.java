package com.baeldung.reactive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.domain.Product;
import com.baeldung.reactive.service.ProductService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        log.info("Get all products invoked.");
        return productService.getProducts();
    }

    @PostMapping
    public Order processOrder(@RequestBody Order order) {
        log.info("Process order invoked with: {}", order);
        return productService.handleOrder(order);
    }

    @DeleteMapping
    public Order revertOrder(@RequestBody Order order) {
        log.info("Revert order invoked with: {}", order);
        return productService.revertOrder(order);
    }
}
