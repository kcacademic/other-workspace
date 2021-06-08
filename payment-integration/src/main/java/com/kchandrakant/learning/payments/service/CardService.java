package com.kchandrakant.learning.payments.service;

import java.util.Optional;
import java.util.Set;

import com.kchandrakant.learning.payments.entity.Card;
import com.kchandrakant.learning.payments.exception.PaymentCardMissingException;

public interface CardService {

    public Optional<Card> getCard(String userId, String cardId) throws PaymentCardMissingException;

    public Optional<Set<Card>> getCards(String userId);

    public Optional<Card> saveCard(String userId, Card card);

    public Optional<Card> deleteCard(String userId, String cardId) throws PaymentCardMissingException;

}
