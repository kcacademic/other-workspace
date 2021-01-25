package com.coc.payments.domain;

import javax.validation.constraints.NotBlank;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table("paymentCard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CardByUserId {

    @PrimaryKeyColumn(name = "user_id", type = PrimaryKeyType.PARTITIONED)
    private String userId;

    @PrimaryKeyColumn(name = "card_id")
    @NotBlank(message = "Card id is mandatory")
    private String cardId;

    @Column("number")
    @NotBlank(message = "Card number is mandatory")
    private String number;

    @Column("expiration_year")
    @NotBlank(message = "Card expiration year is mandatory")
    private String expirationYear;

    @Column("expiration_month")
    @NotBlank(message = "Card expiration month is mandatory")
    private String expirationMonth;

}
