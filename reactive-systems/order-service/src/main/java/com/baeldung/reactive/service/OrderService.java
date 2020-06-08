package com.baeldung.reactive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestTemplate restTemplate;

	public Order createOrder(Order order) {

		Order savedOrder = orderRepository.save(order);

		// Call Inventory Service

		// Call Shipping Service

		return savedOrder;
	}
	
	public List<Order> getOrders() {
		return orderRepository.findAll();
	}

}
