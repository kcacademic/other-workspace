package com.kchandrakant.learning.payments.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kchandrakant.learning.payments.exception.PaymentCardMissingException;
import com.kchandrakant.learning.payments.exception.PaymentCreationException;
import com.kchandrakant.learning.payments.exception.PaymentExecutionException;
import com.kchandrakant.learning.payments.exception.PaymentRecordMissingException;
import com.kchandrakant.learning.payments.service.PaymentService;
import com.kchandrakant.learning.payments.utility.TransformationUtility;
import com.kchandrakant.learning.payments.validation.CardValidator;
import com.kchandrakant.learning.payments.vo.PaymentRequest;
import com.kchandrakant.learning.payments.vo.PaymentResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "Payments API")
public class PaymentController {

    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentService paypalService;

    @Autowired
    PaymentService cybersourceService;
    
    @Autowired
    TransformationUtility utility;

    @PostMapping("/payments/paypal")
    @ApiOperation(value = "Paypal Payment Creation Operation", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Payment with Paypal could not be processed at this moment.") })
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {

        PaymentResponse response = new PaymentResponse();
        try {
            Optional<String> optional = paypalService.createPayment(utility.createPaymentDataFromPaymentRequest(request));
            if (optional.isPresent()) {
                response.setStatus(HttpStatus.OK.getReasonPhrase());
                response.setMessage("Payment with Paypal successfully created, please proceed with authentication.");
                response.setAuthUrl(optional.get());
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                response.setMessage("Payment with Paypal could not be processed at this moment.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
            }
        } catch (PaymentCreationException e) {
            logger.error(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payments/cybersource")
    @ApiOperation(value = "Cybersource Payment Execution Operation", response = String.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "Payment with Cybersource could not be processed at this moment.") })
    public ResponseEntity<PaymentResponse> executeCybersourcePayment(@Valid @RequestBody PaymentRequest request, Errors errors) {
        
        PaymentResponse response = new PaymentResponse();
        
        CardValidator.validate(request.getCard(), errors);
        if (errors.hasErrors()) {
            logger.error(Integer.toString(errors.getErrorCount()));
            response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.setMessage(Integer.toString(errors.getErrorCount()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
        }
        
        try {
            Optional<String> optional = cybersourceService.executePayment(utility.createPaymentDataFromPaymentRequest(request));
            if (optional.isPresent()) {
                response.setStatus(HttpStatus.OK.getReasonPhrase());
                response.setMessage("Payment with Cybersource successfully created.");
                response.setId(optional.get());
            } else {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                response.setMessage("Payment with Cybersource could not be processed at this moment.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
            }
        } catch (PaymentExecutionException | PaymentRecordMissingException | PaymentCardMissingException e) {
            logger.error(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
        }
        return ResponseEntity.ok(response);
    }

}
