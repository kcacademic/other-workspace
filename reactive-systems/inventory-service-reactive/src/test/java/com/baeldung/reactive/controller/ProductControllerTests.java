package com.baeldung.reactive.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.baeldung.reactive.domain.Order;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void test1CreateGithubRepository() {
        Order order = new Order();
        webTestClient.post()
            .uri("/api/products")
            .bodyValue(order)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody()
            .jsonPath("$.orderStatus").isEqualTo("FAILURE")
            .consumeWith(res -> {
                System.out.println(res);
            });
    }

}
