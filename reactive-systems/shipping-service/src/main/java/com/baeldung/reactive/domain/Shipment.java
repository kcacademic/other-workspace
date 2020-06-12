package com.baeldung.reactive.domain;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Shipment {

    private ObjectId id;
    private Date shippingDate;
    private Address address;

}
