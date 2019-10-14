package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.billing.utilities.DataValidation;
import edu.uci.ics.matthes3.service.billing.utilities.DataValidator;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ShoppingCartRequestModel implements DataValidation {
    @JsonProperty(required = true)
    private String email;

    private Integer invalidResultCode = 0;

    public ShoppingCartRequestModel() {
    }

    public ShoppingCartRequestModel(
            @JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }

    @Override
    public void isDataValid() {
        if (!DataValidator.Emails.isLengthValid(email))
            setInvalidResultCode(-10);
        else if (!DataValidator.Emails.isFormatValid(email))
            setInvalidResultCode(-11);
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public Integer getInvalidResultCode() {
        return invalidResultCode;
    }

    public void setInvalidResultCode(Integer invalidResultCode) {
        this.invalidResultCode = invalidResultCode;
    }

}

