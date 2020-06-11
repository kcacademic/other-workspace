package com.baeldung.reactive.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.domain.Shipment;
import com.baeldung.reactive.repository.ShipmentRepository;

@Service
public class ShippingService {

	@Autowired
	ShipmentRepository shipmentRepository;

	public Order handleOrder(Order order) {

		Date shippingDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour <= 10 && hour >= 0) {
			cal.add(Calendar.DATE, 1);
			shippingDate = cal.getTime();
		} else {
			throw new RuntimeException("The current time is off the limits to place order.");
		}

		Shipment shipment = new Shipment();
		shipment.setAddress(order.getShippingAddress());
		shipment.setShippingDate(shippingDate);

		shipmentRepository.save(shipment);
		order.setShippingDate(shippingDate);
		order.setOrderStatus("SUCCESS");

		return order;
	}

}
