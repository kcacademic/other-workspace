package com.baeldung.reactive.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.baeldung.reactive.domain.Shipment;

public interface ShipmentRepository extends MongoRepository<Shipment, ObjectId> {

}
