package com.kchandrakant.learning.payments.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.kchandrakant.learning.payments.domain.PaymentByRequestId;

public interface PaymentRequestRepository extends CassandraRepository<PaymentByRequestId, String> {

}