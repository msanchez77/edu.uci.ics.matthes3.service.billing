package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.matthes3.service.billing.utilities.DataValidation;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class CreditCardRequestModel implements DataValidation {
    @JsonProperty(required=true)
    private String id;

    private Integer invalidResultCode = 0;

    public CreditCardRequestModel() {
    }

    public CreditCardRequestModel(
            @JsonProperty(value="id", required=true) String id) {
        this.id = id;
    }

    @Override
    public void isDataValid() {
        // Id has invalid length (321)
        if (id.length() < 16 || id.length() > 20)
            setInvalidResultCode(321);
        // Id has invalid value (322)
        else if (!id.matches("^[\\d]{16,20}$"))
            setInvalidResultCode(322);
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public Integer getInvalidResultCode() {
        return invalidResultCode;
    }

    public void setInvalidResultCode(Integer invalidResultCode) {
        this.invalidResultCode = invalidResultCode;
    }
}
