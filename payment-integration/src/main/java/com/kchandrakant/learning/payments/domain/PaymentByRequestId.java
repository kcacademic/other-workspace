package com.kchandrakant.learning.payments.domain;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table("paymentRequest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentByRequestId {
    
    @Id
    @PrimaryKey("idempotency_key")
    private String idempotencyKey;
    
    @Column("payment_id")
    @NotBlank(message = "Payment ID is mandatory")
    private String paymentId;
    
    @Column("payment_status")
    @NotBlank(message = "Payment Status is mandatory")
    private String paymentStatus;
    
    @Column("auth_url")
    @NotBlank(message = "Auth URL is mandatory")
    private String authUrl;
    
}
