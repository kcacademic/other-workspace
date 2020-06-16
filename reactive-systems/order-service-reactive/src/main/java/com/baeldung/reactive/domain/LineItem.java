package com.baeldung.reactive.domain;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItem {

    private ObjectId productId;
    private int quantity;

    public LineItem setProductId(ObjectId productId) {
        this.productId = productId;
        return this;
    }

    public LineItem setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

}
