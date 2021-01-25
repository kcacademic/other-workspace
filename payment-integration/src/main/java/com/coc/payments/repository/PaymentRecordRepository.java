package com.coc.payments.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.coc.payments.domain.PaymentByPaymentId;

public interface PaymentRecordRepository extends CassandraRepository<PaymentByPaymentId, String> {

}