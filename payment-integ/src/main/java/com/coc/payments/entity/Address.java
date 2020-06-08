package com.coc.payments.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Address {

    private String firstName;
    private String lastName;
    private String line1;
    private String line2;
    private String city;
    private String countryCode;
    private String postCode;
    private String phone;
    private String state;
    private String email;

}
