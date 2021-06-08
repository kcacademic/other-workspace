package com.kchandrakant.learning.payments.controller;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kchandrakant.learning.payments.entity.Card;
import com.kchandrakant.learning.payments.exception.PaymentCardMissingException;
import com.kchandrakant.learning.payments.service.CardService;
import com.kchandrakant.learning.payments.utility.TransformationUtility;
import com.kchandrakant.learning.payments.vo.PaymentCard;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "Payment Card API")
public class CardController {

    Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    CardService cardService;
    
    @Autowired
    TransformationUtility utility;

    @GetMapping("/payments/cards/{userId}")
    @ApiOperation(value = "Payment Card by User Id Retrival Operation", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Card details for this user could not be fetched.") })
    public ResponseEntity<Set<Card>> getCardsByUser(@PathVariable String userId) {

        Set<Card> cards = Collections.emptySet();
        Optional<Set<Card>> optional = cardService.getCards(userId);
        if (optional.isPresent())
            cards = optional.get();
        return ResponseEntity.ok()
            .body(cards);
    }

    @GetMapping("/payments/cards/{userId}/{cardId}")
    @ApiOperation(value = "Payment Card by Card Id Retrival Operation", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Card details for this card could not be fetched.") })
    public ResponseEntity<Card> getCardsByUser(@PathVariable String userId, @PathVariable String cardId) throws PaymentCardMissingException {

        Card card = null;
        Optional<Card> optional;
        optional = cardService.getCard(userId, cardId);
        if (optional.isPresent())
            card = optional.get();
        return ResponseEntity.ok()
            .body(card);
    }

    @PostMapping("/payments/cards/{userId}")
    @ApiOperation(value = "Payment Card Save Operation", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Card details for this user could not be saved.") })
    public ResponseEntity<Card> saveCard(@PathVariable String userId, @Valid @RequestBody PaymentCard cardRequest) {

        Card card = utility.createCardDataFromCardRequest(cardRequest);
        card.setUserId(userId);
        Optional<Card> optional = cardService.saveCard(userId, card);
        if (optional.isPresent())
            card = optional.get();
        return ResponseEntity.ok()
            .body(card);
    }

    @DeleteMapping("/payments/cards/{userId}/{cardId}")
    @ApiOperation(value = "Payment Card Delete Operation", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Card details for this user could not be deleted.") })
    public ResponseEntity<Card> deleteCard(@PathVariable String userId, @PathVariable String cardId) throws PaymentCardMissingException {

        Card card = null;
        Optional<Card> optional = cardService.deleteCard(userId, cardId);
        if (optional.isPresent())
            card = optional.get();
        return ResponseEntity.ok()
            .body(card);
    }

}
