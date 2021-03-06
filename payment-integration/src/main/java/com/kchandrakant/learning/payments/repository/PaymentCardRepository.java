package com.kchandrakant.learning.payments.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.kchandrakant.learning.payments.domain.CardByUserId;

public interface PaymentCardRepository extends CassandraRepository<CardByUserId, String> {

    List<CardByUserId> findByUserId(String userId);

    Optional<CardByUserId> findByUserIdAndCardId(String userId, String cardId);

}