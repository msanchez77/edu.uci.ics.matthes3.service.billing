package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.utilities.DataValidation;

import java.io.IOException;

public class CustomerRetrieveRequestModel extends CustomerRequestModel {

    public CustomerRetrieveRequestModel() {
    }

    public CustomerRetrieveRequestModel(
            @JsonProperty(value="email", required = true) String email) {
        super(email);
    }


    public static CustomerRetrieveRequestModel isRequestValid(String jsonText) {
        ObjectMapper mapper = new ObjectMapper();
        CustomerRetrieveRequestModel requestModel = new CustomerRetrieveRequestModel();

        if (jsonText.length() == 4) {
            ServiceLogger.LOGGER.warning("No json text found.");
            requestModel.setInvalidResultCode(-1);
            return requestModel;
        }

        try {
            requestModel = mapper.readValue(jsonText, CustomerRetrieveRequestModel.class);
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

    public static String logRequest(CustomerRetrieveRequestModel requestModel) {
        String log = "Email: " + requestModel.getEmail() + "\n";

        return log;
    }
}
