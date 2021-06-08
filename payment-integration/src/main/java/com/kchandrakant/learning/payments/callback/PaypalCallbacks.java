package com.kchandrakant.learning.payments.callback;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kchandrakant.learning.payments.entity.PaymentData;
import com.kchandrakant.learning.payments.exception.PaymentRecordMissingException;
import com.kchandrakant.learning.payments.service.PaymentService;

@RestController
public class PaypalCallbacks {

    Logger logger = LoggerFactory.getLogger(PaypalCallbacks.class);

    @Autowired
    PaymentService paypalService;

    @GetMapping("/process")
    public ResponseEntity<String> authSuccess(@RequestParam String token, @RequestParam String paymentId, @RequestParam(name = "PayerID") String payerID) {
        if (logger.isInfoEnabled())
            logger.info(String.format("Paypal payment authentication passed with details, PaymentId: %s PayerId: %s Token: %s", paymentId, payerID, token));
        String status = "/error";
        try {
            PaymentData paymentData = new PaymentData();
            paymentData.setId(paymentId);
            paymentData.setToken(token);
            paymentData.setPayerId(payerID);
            Optional<String> optional = paypalService.authenticatePayment(paymentData);
            if (optional.isPresent())
                status = optional.get();
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Payment with Paypal could not be processed at this moment.");
        } catch (PaymentRecordMissingException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
        }
        return ResponseEntity.ok(status);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> authFailure(@RequestParam String token) {
        if (logger.isInfoEnabled())
            logger.info(String.format("Paypal payment authentication failed with details, Token: %s", token));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Payment with Paypal could not be processed at this moment.");
    }

}
