package com.sapeint.learning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class MyController {

	@GetMapping("/")
	public Flux<String> getAll() {
		return Flux.just("Hello", "world!").name("mymetric").metrics();
	}

}
