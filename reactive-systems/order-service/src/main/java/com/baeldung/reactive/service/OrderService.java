package com.baeldung.reactive.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${inventory.service.url}")
	private String inventoryServiceUrl;

	@Value("${shipping.service.url}")
	private String shippingServiceUrl;

	public Order createOrder(Order order) {

		order.setLineItems(order.getLineItems().stream().filter(l -> l.getQuantity() > 0).collect(Collectors.toList()));

		Order savedOrder = orderRepository.save(order);

		// Call Inventory Service
		Order inventoryResponse = restTemplate.postForObject(inventoryServiceUrl, order, Order.class);
		System.out.println("Inventory Response: " + inventoryResponse);

		// Call Shipping Service
		Order shippingResponse = restTemplate.postForObject(shippingServiceUrl, order, Order.class);
		System.out.println("Shipping Response: " + shippingResponse);

		if ("SUCCESS".equals(inventoryResponse.getOrderStatus())
				&& "SUCCESS".equals(shippingResponse.getOrderStatus())) {
			savedOrder.setOrderStatus("SUCCESS");
			savedOrder.setShippingDate(shippingResponse.getShippingDate());
		} else
			savedOrder.setOrderStatus("FAILURE");

		orderRepository.save(savedOrder);

		return savedOrder;
	}

	public List<Order> getOrders() {
		return orderRepository.findAll();
	}

}
