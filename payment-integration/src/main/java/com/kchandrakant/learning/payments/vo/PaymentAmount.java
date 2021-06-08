package com.kchandrakant.learning.payments.vo;

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
public class PaymentAmount {
    
    @ApiModelProperty(notes = "The sub total value of the payment to be created.", required = true)
    @NotBlank(message = "Sub total of the order value is mandatory.")
    private String subTotal;
    @ApiModelProperty(notes = "The shipping value of the payment to be created.", required = true)
    @NotBlank(message = "Shipping of the order value is mandatory.")
    private String shipping;
    @ApiModelProperty(notes = "The tax value of the payment to be created.", required = true)
    @NotBlank(message = "Tax of the order value is mandatory.")
    private String tax;
    @ApiModelProperty(notes = "The total value of the payment to be created.", required = true)
    @NotBlank(message = "Total of the order value is mandatory.")
    private String total;
    @ApiModelProperty(notes = "The currency of the payment to be created.", required = true)
    @NotBlank(message = "Currency of the order value is mandatory.")
    private String currency;

}
