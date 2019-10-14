package edu.uci.ics.matthes3.service.billing.models;

public class TransactionFee {
    private String value;
    private String currency;

    public TransactionFee(String value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public String getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }
}
