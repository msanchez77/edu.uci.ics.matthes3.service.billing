package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class CreditCardModel {
    private String id;
    private String firstName;
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date expiration;

    public CreditCardModel() {
    }

    public CreditCardModel(String id, String firstName, String lastName, Date expiration) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getExpiration() {
        return expiration;
    }
}
