package com.baeldung.reactive.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

		boolean success = true;

		Order savedOrder = orderRepository.save(order);

		// Call Inventory Service
		Order inventoryResponse = null;
		try {
			inventoryResponse = restTemplate.postForObject(inventoryServiceUrl, order, Order.class);
			System.out.println("Inventory Response: " + inventoryResponse);
		} catch (Exception ex) {
			success = false;
		}

		// Call Shipping Service
		Order shippingResponse = null;
		try {
			shippingResponse = restTemplate.postForObject(shippingServiceUrl, order, Order.class);
			System.out.println("Shipping Response: " + shippingResponse);
		} catch (Exception ex) {
			success = false;
			HttpEntity<Order> deleteRequest = new HttpEntity<>(order);
			ResponseEntity<Order> deleteResponse = restTemplate.exchange(inventoryServiceUrl, HttpMethod.DELETE,
					deleteRequest, Order.class);
			System.out.println("Inventory Delete Response: " + deleteResponse);
		}

		if (success) {
			savedOrder.setOrderStatus("SUCCESS");
			savedOrder.setShippingDate(shippingResponse.getShippingDate());
		} else {
			savedOrder.setOrderStatus("FAILURE");
		}

		orderRepository.save(savedOrder);

		return savedOrder;
	}

	public List<Order> getOrders() {
		return orderRepository.findAll();
	}

}
