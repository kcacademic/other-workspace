package com.kchandrakant.learning.payments.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.kchandrakant.learning.payments.domain.PaymentByPaymentId;

public interface PaymentRecordRepository extends CassandraRepository<PaymentByPaymentId, String> {

}