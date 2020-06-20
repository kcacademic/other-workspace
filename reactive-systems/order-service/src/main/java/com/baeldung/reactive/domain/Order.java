package com.baeldung.reactive.domain;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.baeldung.reactive.constants.OrderStatus;

import lombok.Data;

@Data
@Document
public class Order {

    @Id
    private ObjectId id;
    private String userId;
    private List<LineItem> lineItems;
    private Long total;
    private String paymentMode;
    private Address shippingAddress;
    private OrderStatus orderStatus;
    private Date shippingDate;

}
