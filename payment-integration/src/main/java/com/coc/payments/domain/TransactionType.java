package com.coc.payments.domain;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import com.datastax.driver.core.DataType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@UserDefinedType("transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransactionType {

    @CassandraType(type = DataType.Name.UUID)
    private UUID id;

    @NotBlank(message = "Transaction Type is mandatory")
    private String type;
    
    private String userId;

    private String amount;
}