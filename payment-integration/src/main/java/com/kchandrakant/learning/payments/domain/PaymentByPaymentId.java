package com.kchandrakant.learning.payments.domain;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.driver.core.DataType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table("paymentRecord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentByPaymentId {

    @Id
    @PrimaryKey("id")
    private String id;

    @Column("payer_id")
    private String payerId;

    @Column("user_id")
    @NotBlank(message = "User ID is mandatory")
    private String userId;

    @Column("idempotency_key")
    private String idempotencyKey;

    @Column("intent")
    private String intent;

    @Column("payment_provider")
    @NotBlank(message = "Payment Provider is mandatory")
    private String paymentProvider;

    @Column("payment_method")
    @NotBlank(message = "Payment Method is mandatory")
    private String paymentMethod;

    @Column("description")
    private String description;

    @Column("payment_status")
    @NotBlank(message = "Payment Status is mandatory")
    private String paymentStatus;

    @CassandraType(type = DataType.Name.UDT, userTypeName = "amount")
    private AmountType amount;

    @CassandraType(type = DataType.Name.UDT, userTypeName = "address")
    private AddressType address;

    @CassandraType(type = DataType.Name.UDT, userTypeName = "transaction")
    private Set<TransactionType> transactions = new HashSet<>();

}
