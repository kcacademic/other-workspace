package com.baeldung.reactive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
	
	@Autowired
	InventoryService inventoryService;
	
	@PostMapping
	public Order process(@RequestBody Order order) {	
		return inventoryService.handleOrder(order);
	}
	
	@DeleteMapping
	public Order revert(@RequestBody Order order) {	
		return inventoryService.revertOrder(order);
	}
	

}
