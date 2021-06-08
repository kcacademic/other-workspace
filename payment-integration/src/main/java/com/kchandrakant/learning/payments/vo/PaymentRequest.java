package com.kchandrakant.learning.payments.vo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentRequest {

    @ApiModelProperty(notes = "The order for which the payment needs to be created.", required = true)
    @NotBlank(message = "Order ID is mandatory.")
    private String orderId;
    @ApiModelProperty(notes = "The idempotency key for the payment request.", required = true)
    @NotBlank(message = "Idempotency Key is mandatory.")
    private String idempotencyKey;
    @ApiModelProperty(notes = "The user ID initiating the payment creation.", required = false)
    private String userId;
    @ApiModelProperty(notes = "The payment provider to create payment (paypal/cybersource).", required = true)
    @NotBlank(message = "Payment Provider is mandatory.")
    private String paymentProvider;
    @ApiModelProperty(notes = "The payment method to use to create payment,(paypal, credit card).", required = true)
    @NotBlank(message = "Payment Method is mandatory.")
    private String paymentMethod;
    @ApiModelProperty(notes = "The intent for the payment.", required = false)
    private String intent;
    @ApiModelProperty(notes = "The description of the payment to be created.", required = false)
    private String description;

    @ApiModelProperty(notes = "The payment details for which the payment needs to be created.", required = true)
    @Valid
    private PaymentAmount amount;
    @ApiModelProperty(notes = "The billing address for which the payment needs to be created.", required = true)
    @Valid
    private PaymentAddress address;
    @ApiModelProperty(notes = "The card data with which the payment needs to be created.", required = true)
    @Valid
    private PaymentCard card;

}
