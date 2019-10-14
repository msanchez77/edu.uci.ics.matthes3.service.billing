package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.utilities.DataValidation;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CCInsertUpdateRequestModel extends CreditCardRequestModel {
    @JsonProperty(required=true)
    private String firstName;

    @JsonProperty(required=true)
    private String lastName;

    @JsonProperty(required=true)
    private Date expiration;

    public CCInsertUpdateRequestModel() {
    }

    public CCInsertUpdateRequestModel(
            @JsonProperty(value="id", required=true) String id,
            @JsonProperty(value="firstName", required=true) String firstName,
            @JsonProperty(value="lastName", required=true) String lastName,
            @JsonProperty(value="expiration", required=true) Date expiration) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("expiration")
    public Date getExpiration() {
        return expiration;
    }

    public static CCInsertUpdateRequestModel isRequestValid(String jsonText) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        mapper.setDateFormat(dateFormat);
        CCInsertUpdateRequestModel requestModel = new CCInsertUpdateRequestModel();

        if (jsonText.length() == 4) {
            ServiceLogger.LOGGER.warning("No json text found.");
            requestModel.setInvalidResultCode(-1);
            return requestModel;
        }

        try {
            requestModel = mapper.readValue(jsonText, CCInsertUpdateRequestModel.class);
            ServiceLogger.LOGGER.info(logRequest(requestModel));

            return requestModel;

        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonParseException)
                requestModel.setInvalidResultCode(-3);
            else if (e instanceof JsonMappingException)
                requestModel.setInvalidResultCode(-2);
            else
                requestModel.setInvalidResultCode(-1);

            return requestModel;
        }
    }

    private static String logRequest(CCInsertUpdateRequestModel requestModel) {
        String log = "Id: " + requestModel.getId() + "\n";
        log += "First Name: " + requestModel.getFirstName() + "\n";
        log += "Last Name: " + requestModel.getLastName() + "\n";
        log += "Expiration: " + requestModel.getExpiration() + "\n";

        return log;
    }
}
