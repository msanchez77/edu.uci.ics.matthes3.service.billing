package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerResponseModel extends ResponseModel {
    @JsonIgnore
    private CustomerModel customer;

    public CustomerResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message) {
        super(resultCode, message);
    }

    public CustomerResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty("customer") CustomerModel customer) {
        super(resultCode, message);
        this.customer = customer;
    }

    @JsonProperty(value="customer")
    public CustomerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerModel customer) {
        this.customer = customer;
    }
}
