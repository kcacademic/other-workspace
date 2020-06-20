package com.baeldung.reactive.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baeldung.reactive.constants.OrderStatus;
import com.baeldung.reactive.domain.Order;
import com.baeldung.reactive.domain.Shipment;
import com.baeldung.reactive.repository.ShipmentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShippingService {

    @Autowired
    ShipmentRepository shipmentRepository;

    public Order handleOrder(Order order) {
        log.info("Handle order invoked with: {}", order);
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
        shipmentRepository.save(new Shipment().setAddress(order.getShippingAddress())
            .setShippingDate(shippingDate));
        return order.setShippingDate(shippingDate)
            .setOrderStatus(OrderStatus.SUCCESS);
    }

}
