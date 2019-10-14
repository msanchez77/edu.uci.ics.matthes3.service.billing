package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.utilities.ModelMaintenance;

import java.io.IOException;

public class SCRetrieveClearRequestModel extends ShoppingCartRequestModel {

    public SCRetrieveClearRequestModel() {
    }

    public SCRetrieveClearRequestModel(
            @JsonProperty(value="email", required = true) String email) {
        super(email);
    }


    public static SCRetrieveClearRequestModel isRequestValid(String jsonText) {
        ObjectMapper mapper = new ObjectMapper();
        SCRetrieveClearRequestModel requestModel = new SCRetrieveClearRequestModel();

        if (jsonText.length() == 4) {
            ServiceLogger.LOGGER.warning("No json text found.");
            requestModel.setInvalidResultCode(-1);
            return requestModel;
        }

        try {
            requestModel = mapper.readValue(jsonText, SCRetrieveClearRequestModel.class);
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

    public static String logRequest(SCRetrieveClearRequestModel requestModel) {
        String log = "Email: " + requestModel.getEmail() + "\n";

        return log;
    }
}