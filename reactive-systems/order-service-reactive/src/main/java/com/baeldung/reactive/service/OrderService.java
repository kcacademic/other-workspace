package com.baeldung.reactive.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.baeldung.reactive.constants.OrderStatus;
import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Value("${shipping.service.url}")
    private String shippingServiceUrl;

    @Autowired
    private ExchangeStrategies customExchangeStrategies;

    private WebClient webClient = WebClient.builder()
        .exchangeStrategies(customExchangeStrategies)
        .build();

    public Mono<Order> createOrder(Order order) {
        log.info("Create order invoked with: {}", order);
        return Mono.just(order)
            .map(o -> {
                return o.setLineItems(o.getLineItems()
                    .stream()
                    .filter(l -> l.getQuantity() > 0)
                    .collect(Collectors.toList()));
            })
            .flatMap(orderRepository::save)
            .flatMap(o -> {
                return webClient.method(HttpMethod.POST)
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
                                    log.error("Inventory Error: " + new BufferedReader(new InputStreamReader(b.asInputStream())).lines()
                                        .collect(Collectors.joining("\n")));
                                })
                                .subscribe();
                            return Mono.error(new RuntimeException("Inventory Call Failed :" + clientResponse.statusCode()
                                .getReasonPhrase()));
                        } else
                            return clientResponse.bodyToMono(Order.class);
                    });
            })
            .onErrorResume(err -> {
                return Mono.just(order.setOrderStatus(OrderStatus.FAILURE)
                    .setResponseMessage(err.getMessage()));
            })
            .flatMap(o -> {
                if (!OrderStatus.FAILURE.equals(o.getOrderStatus()))
                    return webClient.method(HttpMethod.POST)
                        .uri(shippingServiceUrl)
                        .body(BodyInserters.fromValue(o))
                        .exchange()
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode()
                                .is5xxServerError()) {
                                clientResponse.body((clientHttpResponse, context) -> {
                                    return clientHttpResponse.getBody();
                                })
                                    .doOnNext(b -> {
                                        log.error("Shipping Error: " + new BufferedReader(new InputStreamReader(b.asInputStream())).lines()
                                            .collect(Collectors.joining("\n")));
                                    })
                                    .subscribe();
                                return Mono.error(new RuntimeException("Shipping Call Failed :" + clientResponse.statusCode()
                                    .getReasonPhrase()));
                            } else
                                return clientResponse.bodyToMono(Order.class);
                        });
                else
                    return Mono.just(o);
            })
            .onErrorResume(err -> {
                return webClient.method(HttpMethod.POST)
                    .uri(inventoryServiceUrl)
                    .body(BodyInserters.fromValue(order))
                    .retrieve()
                    .bodyToMono(Order.class)
                    .doOnError(e -> {
                        log.error("Inventory Revert Call Failed :" + e.getMessage());
                    })
                    .map(o -> o.setOrderStatus(OrderStatus.FAILURE)
                        .setResponseMessage(err.getMessage()));
            })
            .map(o -> {
                if (!OrderStatus.FAILURE.equals(o.getOrderStatus()))
                    return order.setShippingDate(o.getShippingDate())
                        .setOrderStatus(OrderStatus.SUCCESS);
                else
                    return order.setOrderStatus(OrderStatus.FAILURE)
                        .setResponseMessage(o.getResponseMessage());
            })
            .flatMap(orderRepository::save);
    }

    public Flux<Order> getOrders() {
        log.info("Get all orders invoked.");
        return orderRepository.findAll();
    }
}
