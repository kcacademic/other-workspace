package com.baeldung.reactive.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItem {

    private String productId;
    private int quantity;

    public LineItem setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public LineItem setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

}
