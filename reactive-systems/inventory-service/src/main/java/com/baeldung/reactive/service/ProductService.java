package com.baeldung.reactive.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.domain.Product;
import com.baeldung.reactive.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public Order handleOrder(Order order) {
        log.info("Handle order invoked with: {}", order);
        order.getLineItems()
            .stream()
            .map(l -> {
                Optional<Product> o = productRepository.findById(l.getProductId());
                if (o.isPresent()) {
                    Product p = o.get();
                    if (p.getStock() >= l.getQuantity()) {
                        p.setStock(p.getStock() - l.getQuantity());
                        productRepository.save(p);
                    } else {
                        throw new RuntimeException("Product is out of stock: " + l.getProductId());
                    }
                }
                return l;
            })
            .collect(Collectors.toList());
        order.setOrderStatus("SUCCESS");
        return order;
    }

    @Transactional
    public Order revertOrder(Order order) {
        log.info("Revert order invoked with: {}", order);
        order.getLineItems()
            .stream()
            .map(l -> {
                Optional<Product> o = productRepository.findById(l.getProductId());
                if (o.isPresent()) {
                    Product p = o.get();
                    p.setStock(p.getStock() + l.getQuantity());
                    productRepository.save(p);
                }
                return l;
            })
            .collect(Collectors.toList());

        order.setOrderStatus("SUCCESS");
        return order;
    }
}
