package com.baeldung.reactive.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.baeldung.reactive.domain.Product;

public interface ProductRepository extends MongoRepository<Product, ObjectId> {

}
