package com.baeldung.reactive.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.repository.OrderRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Value("${shipping.service.url}")
    private String shippingServiceUrl;

    public Mono<Order> createOrder(Order order) {

        return Mono.just(order)
            .map(o -> {
                return o.setLineItems(o.getLineItems()
                    .stream()
                    .filter(l -> l.getQuantity() > 0)
                    .collect(Collectors.toList()));
            })
            // .flatMap(orderRepository::save)
            .flatMap(o -> {
                return WebClient.create()
                    .method(HttpMethod.POST)
                    .uri(inventoryServiceUrl)
                    .body(BodyInserters.fromValue(o))
                    .exchange()
                    .flatMap(clientResponse -> {
                        if (clientResponse.statusCode()
                            .is5xxServerError()) {
                            clientResponse.body((clientHttpResponse, context) -> {
                                return clientHttpResponse.getBody();
                            })
                                .doOnNext(b -> {
                                    System.out.println("Inventory Error: " + new BufferedReader(new InputStreamReader(b.asInputStream())).lines()
                                        .collect(Collectors.joining("\n")));
                                })
                                .subscribe();
                            throw new RuntimeException("Inventory Call Failed :" + clientResponse.statusCode()
                                .getReasonPhrase());
                        } else
                            return clientResponse.bodyToMono(Order.class);
                    });
            })
            .onErrorResume(err -> {
                return Mono.just(order.setOrderStatus("FAILURE")
                    .setResponseMessage(err.getMessage()));
            })
            .flatMap(o -> {
                if (!"FAILURE".equals(o.getOrderStatus()))
                    return WebClient.create(shippingServiceUrl)
                        .post()
                        .body(BodyInserters.fromValue(o))
                        .exchange()
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode()
                                .is5xxServerError()) {
                                clientResponse.body((clientHttpResponse, context) -> {
                                    return clientHttpResponse.getBody();
                                })
                                    .doOnNext(b -> {
                                        System.out.println("Shipping Error: " + new BufferedReader(new InputStreamReader(b.asInputStream())).lines()
                                            .collect(Collectors.joining("\n")));
                                    })
                                    .subscribe();
                                throw new RuntimeException("Shipping Call Failed :" + clientResponse.statusCode()
                                    .getReasonPhrase());
                            } else
                                return clientResponse.bodyToMono(Order.class);
                        });
                else
                    return Mono.just(o);
            })
            .onErrorResume(err -> {
                return WebClient.create()
                    .method(HttpMethod.DELETE)
                    .uri(inventoryServiceUrl)
                    .body(BodyInserters.fromValue(order))
                    .retrieve()
                    .bodyToMono(Order.class)
                    .doOnError(e -> {
                        System.out.println("Inventory Revert Call Failed :" + e.getMessage());
                    })
                    .map(o -> o.setOrderStatus("FAILURE")
                        .setResponseMessage(err.getMessage()));
            })
            .map(o -> {
                if (!"FAILURE".equals(o.getOrderStatus()))
                    return order.setShippingDate(o.getShippingDate())
                        .setOrderStatus("SUCCESS");
                else
                    return order.setOrderStatus("FAILURE")
                        .setResponseMessage(o.getResponseMessage());
            })
            .flatMap(orderRepository::save);
    }

    public Flux<Order> getOrders() {
        return orderRepository.findAll();
    }
}
