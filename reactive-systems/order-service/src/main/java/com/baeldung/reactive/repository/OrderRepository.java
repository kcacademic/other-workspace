package com.baeldung.reactive.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.baeldung.reactive.domain.Order;

public interface OrderRepository extends MongoRepository<Order, ObjectId> {

}
