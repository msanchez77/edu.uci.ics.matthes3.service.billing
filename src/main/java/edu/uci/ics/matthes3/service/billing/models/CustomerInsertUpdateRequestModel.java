package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.utilities.DataValidation;

import java.io.IOException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerInsertUpdateRequestModel extends CustomerRequestModel implements DataValidation {
    @JsonProperty(required=true)
    private String firstName;

    @JsonProperty(required=true)
    private String lastName;

    @JsonProperty(required=true)
    private String ccId;

    @JsonProperty(required=true)
    private String address;

    public CustomerInsertUpdateRequestModel() {
    }

    public CustomerInsertUpdateRequestModel(
            @JsonProperty(value="email", required=true) String email,
            @JsonProperty(value="firstName", required=true) String firstName,
            @JsonProperty(value="lastName", required=true) String lastName,
            @JsonProperty(value="ccId", required=true) String ccId,
            @JsonProperty(value="address", required=true) String address) {
        super(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.ccId = ccId;
        this.address = address;
    }

    @Override
    public void isDataValid() {
        // Id has invalid length (321)
        if (ccId.length() < 16 || ccId.length() > 20)
            setInvalidResultCode(321);
            // Id has invalid value (322)
        else if (!ccId.matches("^[\\d]{16,20}$"))
            setInvalidResultCode(322);
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("ccId")
    public String getCcId() {
        return ccId;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    public static CustomerInsertUpdateRequestModel isRequestValid(String jsonText) {
        ObjectMapper mapper = new ObjectMapper();
        CustomerInsertUpdateRequestModel requestModel = new CustomerInsertUpdateRequestModel();

        if (jsonText.length() == 4) {
            ServiceLogger.LOGGER.warning("No json text found.");
            requestModel.setInvalidResultCode(-1);
            return requestModel;
        }

        try {
            requestModel = mapper.readValue(jsonText, CustomerInsertUpdateRequestModel.class);
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

    private static String logRequest(CustomerInsertUpdateRequestModel requestModel) {
        String log = "Email: " + requestModel.getEmail() + "\n";
        log += "First Name: " + requestModel.getFirstName() + "\n";
        log += "Last Name: " + requestModel.getLastName() + "\n";
        log += "ccId: " + requestModel.getCcId() + "\n";
        log += "Address: " + requestModel.getAddress() + "\n";

        return log;
    }
}
