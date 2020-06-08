package com.baeldung.reactive.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Product {
	
	private ObjectId id;
	private String name;
	private Long price;
	private Integer stock;

}
