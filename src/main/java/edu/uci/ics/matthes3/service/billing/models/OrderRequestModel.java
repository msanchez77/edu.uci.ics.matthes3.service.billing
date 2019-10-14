package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;

import java.io.IOException;

public class OrderRequestModel extends CustomerRequestModel {

    public OrderRequestModel() {
    }

    public OrderRequestModel(
            @JsonProperty(value="email", required = true) String email) {
        super(email);
    }

    public static OrderRequestModel isRequestValid(String jsonText) {
        ObjectMapper mapper = new ObjectMapper();
        OrderRequestModel requestModel = new OrderRequestModel();

        if (jsonText.length() == 4) {
            ServiceLogger.LOGGER.warning("No json text found.");
            requestModel.setInvalidResultCode(-1);
            return requestModel;
        }

        try {
            requestModel = mapper.readValue(jsonText, OrderRequestModel.class);
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

    public static String logRequest(OrderRequestModel requestModel) {
        String log = "Email: " + requestModel.getEmail() + "\n";

        return log;
    }
}
