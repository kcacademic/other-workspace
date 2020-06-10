package com.baeldung.reactive.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.domain.Product;
import com.baeldung.reactive.repository.ProductRepository;

@Service
public class InventoryService {

	@Autowired
	ProductRepository productRepository;

	public Order handleOrder(Order order) {

		order.getLineItems().stream().map(l -> {
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
		}).collect(Collectors.toList());

		order.setOrderStatus("SUCCESS");
		return order;
	}

}
