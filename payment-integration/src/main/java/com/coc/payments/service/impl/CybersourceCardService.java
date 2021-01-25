package com.coc.payments.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coc.payments.domain.CardByUserId;
import com.coc.payments.entity.Card;
import com.coc.payments.exception.PaymentCardMissingException;
import com.coc.payments.repository.PaymentCardRepository;
import com.coc.payments.service.CardService;
import com.coc.payments.utility.TransformationUtility;

@Service
public class CybersourceCardService implements CardService {

    @Autowired
    PaymentCardRepository repository;
    
    @Autowired
    TransformationUtility utility;

    @Override
    public Optional<Card> getCard(String userId, String cardId) throws PaymentCardMissingException {
        Optional<CardByUserId> savedCard = repository.findByUserIdAndCardId(userId, cardId);
        if (savedCard.isPresent())
            return Optional.of(utility.createCardFromPaymentCard(savedCard.get()));
        else
            throw new PaymentCardMissingException(String.format("The payment card with id: %s could not be found.", cardId));
    }

    @Override
    public Optional<Set<Card>> getCards(String userId) {
        List<CardByUserId> savedCards = repository.findByUserId(userId);
        Set<Card> cards = savedCards.stream()
            .map(utility::createCardFromPaymentCard)
            .collect(Collectors.toSet());
        return Optional.of(cards);
    }

    @Override
    public Optional<Card> saveCard(String userId, Card card) {

        Optional<Set<Card>> savedCards = getCards(userId);
        boolean saved = false;
        if (savedCards.isPresent()) {
            for (Card savedCard : savedCards.get()) {
                if (card.getNumber()
                    .equals(savedCard.getNumber()))
                    saved = true;
            }
        }
        CardByUserId cardSaved = null;
        if (!saved) {
            cardSaved = repository.save(utility.createPaymentCardFromCardData(card));
            return Optional.of(utility.createCardFromPaymentCard(cardSaved));
        } else
            return Optional.ofNullable(null);
    }

    @Override
    public Optional<Card> deleteCard(String userId, String cardId) throws PaymentCardMissingException {

        Optional<CardByUserId> savedCard = repository.findByUserIdAndCardId(userId, cardId);
        if (savedCard.isPresent()) {
            repository.delete(savedCard.get());
            return Optional.of(utility.createCardFromPaymentCard(savedCard.get()));
        } else
            throw new PaymentCardMissingException(String.format("The payment card with id: %s could not be found.", cardId));
    }

}
