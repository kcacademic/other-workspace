package com.coc.payments.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.coc.payments.entity.PaymentData;
import com.coc.payments.exception.PaymentCreationException;
import com.coc.payments.service.PaymentService;
import com.coc.payments.vo.PaymentAddress;
import com.coc.payments.vo.PaymentAmount;
import com.coc.payments.vo.PaymentRequest;

@RunWith(SpringJUnit4ClassRunner.class)
public class PaymentControllerUnitTests {

    @Mock
    PaymentService paymentService;

    @InjectMocks
    PaymentController paymentController;

    @Test
    public void givenPaymentController_whenCreatePaymentCalled_thenValidRedirectUrl() throws PaymentCreationException {
        PaymentRequest request = new PaymentRequest();
        PaymentAmount amount = new PaymentAmount();
        request.setAmount(amount);
        PaymentAddress address = new PaymentAddress();
        request.setAddress(address);
        String authUrl = "https://paypal.com/auth";
        when(paymentService.createPayment(any(PaymentData.class))).thenReturn(Optional.ofNullable(authUrl));
        assertEquals(authUrl, paymentController.createPayment(request).getBody().getAuthUrl());
    }

}
