package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class CCDeleteRetrieveRequestModel extends CreditCardRequestModel {

    public CCDeleteRetrieveRequestModel() {
    }

    public CCDeleteRetrieveRequestModel(
            @JsonProperty(value="id", required = true) String id) {
        super(id);
    }

    public static CCDeleteRetrieveRequestModel isRequestValid(String jsonText) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        mapper.setDateFormat(dateFormat);
        CCDeleteRetrieveRequestModel requestModel = new CCDeleteRetrieveRequestModel();

        if (jsonText.length() == 4) {
            ServiceLogger.LOGGER.warning("No json text found.");
            requestModel.setInvalidResultCode(-1);
            return requestModel;
        }

        try {
            requestModel = mapper.readValue(jsonText, CCDeleteRetrieveRequestModel.class);
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

    public static String logRequest(CCDeleteRetrieveRequestModel requestModel) {
        String log = "Id: " + requestModel.getId() + "\n";

        return log;
    }
}
