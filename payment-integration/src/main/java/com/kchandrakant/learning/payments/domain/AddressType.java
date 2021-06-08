package com.kchandrakant.learning.payments.domain;

import javax.validation.constraints.NotBlank;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@UserDefinedType("address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressType {
    
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Address line is mandatory")
    private String line1;
    private String line2;
    @NotBlank(message = "City is mandatory")
    private String city;
    @NotBlank(message = "Country Code is mandatory")
    private String countryCode;
    @NotBlank(message = "Postal Code is mandatory")
    private String postCode;
    private String phone;
    @NotBlank(message = "State is mandatory")
    private String state;
    private String email;
    
}