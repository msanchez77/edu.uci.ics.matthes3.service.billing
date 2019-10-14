package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;

import java.io.IOException;

public class SCDeleteRequestModel extends ShoppingCartRequestModel {
    @JsonProperty(required=true)
    private String movieId;

    public SCDeleteRequestModel() {
    }

    public SCDeleteRequestModel(
            @JsonProperty(value="email", required = true) String email,
            @JsonProperty(value="movieId", required = true) String movieId) {
        super(email);
        this.movieId = movieId;
    }

    @JsonProperty("movieId")
    public String getMovieId() {
        return movieId;
    }

    public static SCDeleteRequestModel isRequestValid(String jsonText) {
        ObjectMapper mapper = new ObjectMapper();
        SCDeleteRequestModel requestModel = new SCDeleteRequestModel();

        if (jsonText.length() == 4) {
            ServiceLogger.LOGGER.warning("No json text found.");
            requestModel.setInvalidResultCode(-1);
            return requestModel;
        }

        try {
            requestModel = mapper.readValue(jsonText, SCDeleteRequestModel.class);
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

    public static String logRequest(SCDeleteRequestModel requestModel) {
        String log = "Email: " + requestModel.getEmail() + "\n";
        log += "MovieID: " + requestModel.getMovieId() + "\n";

        return log;
    }
}