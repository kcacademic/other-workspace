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
public class PaymentAddress {
    
    @ApiModelProperty(notes = "The name of the billing address.", required = true)
    @NotBlank(message = "Name as part of billing address is mandatory.")
    private String name;
    @ApiModelProperty(notes = "The first line of the billing address.", required = true)
    @NotBlank(message = "Addess line as part of billing address is mandatory.")
    private String line1;
    @ApiModelProperty(notes = "The second line of the billing address.", required = false)
    private String line2;
    @ApiModelProperty(notes = "The city of the billing address.", required = true)
    @NotBlank(message = "City name as part of billing address is mandatory.")
    private String city;
    @ApiModelProperty(notes = "The country code of the billing address.", required = true)
    @NotBlank(message = "Country Code as part of billing address is mandatory.")
    private String countryCode;
    @ApiModelProperty(notes = "The postal code of the billing address.", required = true)
    @NotBlank(message = "Post Code as part of billing address is mandatory.")
    private String postCode;
    @ApiModelProperty(notes = "The phone number of the billing address.", required = false)
    private String phone;
    @ApiModelProperty(notes = "The state code of the billing address.", required = true)
    @NotBlank(message = "State as part of billing address is mandatory.")
    private String state;
    @ApiModelProperty(notes = "The email of the billing address.", required = false)
    private String email;

}
