package com.kchandrakant.learning.payments.vo;

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
public class PaymentResponse {
    
    private String id;
    private String status;
    private String message;
    private String authUrl;

}
