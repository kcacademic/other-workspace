package com.coc.payments.utility;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coc.payments.constant.PaymentConstant;
import com.coc.payments.domain.AddressType;
import com.coc.payments.domain.AmountType;
import com.coc.payments.domain.CardByUserId;
import com.coc.payments.domain.PaymentByPaymentId;
import com.coc.payments.domain.PaymentByRequestId;
import com.coc.payments.domain.TransactionType;
import com.coc.payments.entity.Address;
import com.coc.payments.entity.Amount;
import com.coc.payments.entity.Card;
import com.coc.payments.entity.PaymentData;
import com.coc.payments.event.PaymentEvent;
import com.coc.payments.vo.PaymentCard;
import com.coc.payments.vo.PaymentRequest;

@Component
public class TransformationUtility {

    @Autowired
    CryptoUtility cryptoUtility;

    public PaymentData createPaymentDataFromPaymentRequest(PaymentRequest request) {

        PaymentData payment = new PaymentData();

        payment.setIdempotencyKey(request.getIdempotencyKey());
        payment.setUserId(request.getUserId());
        payment.setPaymentProvider(request.getPaymentProvider());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setIntent(request.getIntent());
        payment.setDescription(request.getDescription());

        Amount amount = new Amount();
        amount.setCurrency(request.getAmount()
            .getCurrency());
        amount.setTotal(request.getAmount()
            .getTotal());
        amount.setSubTotal(request.getAmount()
            .getSubTotal());
        amount.setShipping(request.getAmount()
            .getShipping());
        amount.setTax(request.getAmount()
            .getTax());
        payment.setAmount(amount);

        Address address = new Address();
        address.setFirstName((request.getAddress()
            .getName()));
        address.setLastName((request.getAddress()
            .getName()));
        address.setLine1(request.getAddress()
            .getLine1());
        address.setLine2(request.getAddress()
            .getLine2());
        address.setCity(request.getAddress()
            .getCity());
        address.setPostCode(request.getAddress()
            .getPostCode());
        address.setCountryCode(request.getAddress()
            .getCountryCode());
        address.setState(request.getAddress()
            .getState());
        address.setPhone(request.getAddress()
            .getPhone());
        address.setEmail(request.getAddress()
            .getEmail());
        payment.setAddress(address);

        if (request.getCard() != null) {
            Card card = new Card();
            card.setUserId(request.getUserId());
            card.setCardId(request.getCard()
                .getCardId());
            card.setNumber(request.getCard()
                .getNumber());
            card.setExpirationYear(request.getCard()
                .getExpirationYear());
            card.setExpirationMonth(request.getCard()
                .getExpirationMonth());
            card.setSecurityCode(request.getCard()
                .getSecurityCode());
            card.setSaveCard(request.getCard()
                .isSaveCard());
            payment.setCard(card);
        }

        return payment;
    }

    public Card createCardDataFromCardRequest(PaymentCard cardRequest) {

        Card card = new Card();
        card.setNumber(cardRequest.getNumber());
        card.setExpirationYear(cardRequest.getExpirationYear());
        card.setExpirationMonth(cardRequest.getExpirationMonth());
        card.setSecurityCode(cardRequest.getSecurityCode());
        card.setSaveCard(cardRequest.isSaveCard());

        return card;
    }

    public PaymentByPaymentId createPaymentRecordFromPaymentData(PaymentData paymentData) {

        TransactionType transaction = new TransactionType();
        transaction.setId(UUID.randomUUID());
        transaction.setType(PaymentConstant.PAYMENT_EXECUTED);
        transaction.setUserId(paymentData.getUserId());
        transaction.setAmount(paymentData.getAmount()
            .getTotal());
        AmountType amount = new AmountType();
        amount.setCurrency(paymentData.getAmount()
            .getCurrency());
        amount.setTotal(paymentData.getAmount()
            .getTotal());
        amount.setSubTotal(paymentData.getAmount()
            .getSubTotal());
        amount.setShipping(paymentData.getAmount()
            .getShipping());
        amount.setTax(paymentData.getAmount()
            .getTax());
        AddressType address = new AddressType();
        address.setName(paymentData.getAddress()
            .getFirstName() + " "
            + paymentData.getAddress()
                .getLastName());
        address.setLine1(paymentData.getAddress()
            .getLine1());
        address.setLine2(paymentData.getAddress()
            .getLine2());
        address.setCity(paymentData.getAddress()
            .getCity());
        address.setPostCode(paymentData.getAddress()
            .getPostCode());
        address.setCountryCode(paymentData.getAddress()
            .getCountryCode());
        address.setState(paymentData.getAddress()
            .getState());
        address.setPhone(paymentData.getAddress()
            .getPhone());
        address.setEmail(paymentData.getAddress()
            .getEmail());

        PaymentByPaymentId record = new PaymentByPaymentId();
        record.setId(paymentData.getId());
        record.setIdempotencyKey(paymentData.getIdempotencyKey());
        record.setIntent(paymentData.getIntent());
        record.setPaymentStatus(PaymentConstant.PAYMENT_EXECUTED);
        record.setPaymentProvider(paymentData.getPaymentProvider());
        record.setPaymentMethod(paymentData.getPaymentMethod());
        record.setDescription(paymentData.getDescription());
        record.setUserId(paymentData.getUserId());

        record.getTransactions()
            .add(transaction);
        record.setAmount(amount);
        record.setAddress(address);

        return record;
    }

    public PaymentByRequestId createPaymentRequestFromPaymentData(PaymentData paymentData) {

        PaymentByRequestId request = new PaymentByRequestId();
        request.setIdempotencyKey(paymentData.getIdempotencyKey());
        request.setPaymentId(paymentData.getId());
        request.setPaymentStatus(paymentData.getState());
        request.setAuthUrl(paymentData.getAuthUrl());

        return request;
    }

    public CardByUserId createPaymentCardFromCardData(Card card) {

        CardByUserId paymentCard = new CardByUserId();
        paymentCard.setCardId(UUID.randomUUID()
            .toString());
        paymentCard.setUserId(card.getUserId());
        paymentCard.setNumber(cryptoUtility.encrypt(card.getNumber()));
        paymentCard.setExpirationYear(cryptoUtility.encrypt(card.getExpirationYear()));
        paymentCard.setExpirationMonth(cryptoUtility.encrypt(card.getExpirationMonth()));

        return paymentCard;
    }

    public Card createCardFromPaymentCard(CardByUserId paymentCard) {

        Card card = new Card();
        card.setCardId(paymentCard.getCardId());
        card.setUserId(paymentCard.getUserId());
        String number = cryptoUtility.decrypt(paymentCard.getNumber());
        number = StringUtils.overlay(number, StringUtils.repeat("x", number.length()-4), 0, number.length()-4);
        card.setNumber(number);
        card.setExpirationYear(cryptoUtility.decrypt(paymentCard.getExpirationYear()));
        card.setExpirationMonth(cryptoUtility.decrypt(paymentCard.getExpirationMonth()));
        card.setSaveCard(true);
        
        return card;
    }

    public PaymentEvent createPaymentEvent(PaymentData paymentData, String eventType) {

        PaymentEvent event = new PaymentEvent();
        event.setId(paymentData.getId());
        event.setUserId(paymentData.getUserId());
        event.setPaymentProvider(paymentData.getPaymentProvider());
        event.setPaymentMethod(paymentData.getPaymentMethod());
        event.setAmount(paymentData.getAmount()
            .getTotal());
        event.setEventType(eventType);

        return event;
    }

}
