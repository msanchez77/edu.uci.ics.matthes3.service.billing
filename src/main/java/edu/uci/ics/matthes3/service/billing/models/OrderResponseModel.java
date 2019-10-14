package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderResponseModel extends ResponseModel {
    @JsonIgnore
    private String redirectURL;
    @JsonIgnore
    private String token;

    @JsonIgnore
    private TransactionModel[] transactions;

    public OrderResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message) {
        super(resultCode, message);
    }

    public OrderResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty(value = "redirectURL") String redirectURL,
            @JsonProperty(value="token") String token) {
        super(resultCode, message);
        this.redirectURL = redirectURL;
        this.token = token;
    }

    public OrderResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty(value = "transactions") TransactionModel[] transactions) {
        super(resultCode, message);
        this.transactions = transactions;
    }

    @JsonProperty(value="redirectURL")
    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    @JsonProperty(value="token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty(value="transactions")
    public TransactionModel[] getTransactions() {
        return transactions;
    }

    public void setTransactions(TransactionModel[] transactions) {
        this.transactions = transactions;
    }
}
