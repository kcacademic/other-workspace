package com.baeldung.reactive.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.domain.Product;
import com.baeldung.reactive.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Flux<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Mono<Order> handleOrder(Order order) {

        return Flux.fromIterable(order.getLineItems())
            .flatMap(l -> productRepository.findById(l.getProductId()))
            .flatMap(p -> {
                int q = order.getLineItems()
                    .stream()
                    .filter(l -> l.getProductId()
                        .equals(p.getId()))
                    .collect(Collectors.toList())
                    .get(0)
                    .getQuantity();
                if (p.getStock() >= q) {
                    p.setStock(p.getStock() - q);
                    return productRepository.save(p);
                } else {
                    throw new RuntimeException("Product is out of stock: " + p.getId());
                }
            })
            .then(Mono.just(order.setOrderStatus("SUCCESS")));
    }

    @Transactional
    public Mono<Order> revertOrder(Order order) {

        return Flux.fromIterable(order.getLineItems())
            .flatMap(l -> productRepository.findById(l.getProductId()))
            .flatMap(p -> {
                int q = order.getLineItems()
                    .stream()
                    .filter(l -> l.getProductId()
                        .equals(p.getId()))
                    .collect(Collectors.toList())
                    .get(0)
                    .getQuantity();

                p.setStock(p.getStock() + q);
                return productRepository.save(p);
            })
            .then(Mono.just(order.setOrderStatus("SUCCESS")));
    }

}
