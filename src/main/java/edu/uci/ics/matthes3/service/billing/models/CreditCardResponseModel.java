package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditCardResponseModel extends ResponseModel {
    @JsonIgnore
    private CreditCardModel creditCard;

    public CreditCardResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message) {
        super(resultCode, message);
    }

    public CreditCardResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty("creditcard") CreditCardModel creditCard) {
        super(resultCode, message);
        this.creditCard = creditCard;
    }

    @JsonProperty(value="creditcard")
    public CreditCardModel getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCardModel creditCard) {
        this.creditCard = creditCard;
    }
}
