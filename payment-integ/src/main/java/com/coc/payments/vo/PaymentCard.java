package com.coc.payments.vo;

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
public class PaymentCard {

    @ApiModelProperty(notes = "The card Id of the saved card to use.", required = false)
    //@NotBlank(message = "Card Id of the saved card is mandatory.")
    private String cardId;
    @ApiModelProperty(notes = "The number of the card being used.", required = false)
    //@NotBlank(message = "Number of the card is mandatory.")
    private String number;
    @ApiModelProperty(notes = "The expiration year of the card being used.", required = false)
    //@NotBlank(message = "Expiration Year of the card is mandatory.")
    private String expirationYear;
    @ApiModelProperty(notes = "The expiration month of the card being used.", required = false)
    //@NotBlank(message = "Expiration Month of the card is mandatory.")
    private String expirationMonth;
    @ApiModelProperty(notes = "The security code of the card being used.", required = true)
    @NotBlank(message = "Security Code of the card is mandatory.")
    private String securityCode;
    @ApiModelProperty(notes = "Tha flag which indicates if the card data needs to be saved.", required = false)
    private boolean saveCard;

}
