package com.coc.payments.domain;

import javax.validation.constraints.NotBlank;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@UserDefinedType("amount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AmountType {

    @NotBlank(message = "Sub Total is mandatory")
    private String subTotal;
    @NotBlank(message = "Shipping is mandatory")
    private String shipping;
    @NotBlank(message = "Tax is mandatory")
    private String tax;
    @NotBlank(message = "Total is mandatory")
    private String total;
    @NotBlank(message = "Currency is mandatory")
    private String currency;

}