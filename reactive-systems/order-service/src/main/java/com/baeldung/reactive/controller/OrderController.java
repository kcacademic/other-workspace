package com.baeldung.reactive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.service.OrderService;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping
	public Order create(@RequestBody Order order) {
		Order processedOrder = orderService.createOrder(order);
		if ("FAILURE".equals(processedOrder.getOrderStatus()))
			throw new RuntimeException("Order processing failed.");
		return processedOrder;
	}

	@GetMapping
	public List<Order> getAll() {
		return orderService.getOrders();
	}

}
