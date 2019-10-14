package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;

import java.io.IOException;

public class OrderCompleteRequestModel {
    @JsonProperty(required=true)
    private String paymentId;
    @JsonProperty(required=true)
    private String token;
    @JsonProperty(required=true)
    private String PayerID;

    public OrderCompleteRequestModel() {
    }

    public OrderCompleteRequestModel(String paymentId, String token, String PayerID) {
        this.paymentId = paymentId;
        this.token = token;
        this.PayerID = PayerID;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getToken() {
        return token;
    }

    public String getPayerID() {
        return PayerID;
    }
}
