package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;

import java.io.IOException;

public class SCInsertUpdateRequestModel extends ShoppingCartRequestModel {
    @JsonProperty(required=true)
    private String movieId;

    @JsonProperty(required=true)
    private Integer quantity;

    public SCInsertUpdateRequestModel() {
    }

    public SCInsertUpdateRequestModel(
            @JsonProperty(value="email", required = true) String email,
            @JsonProperty(value="movieId", required = true) String movieId,
            @JsonProperty(value="quantity", required = true) Integer quantity) {
        super(email);
        this.movieId = movieId;
        this.quantity = quantity;
    }

    @JsonProperty("movieId")
    public String getMovieId() {
        return movieId;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    public static SCInsertUpdateRequestModel isRequestValid(String jsonText) {
        ObjectMapper mapper = new ObjectMapper();
        SCInsertUpdateRequestModel requestModel = new SCInsertUpdateRequestModel();

        if (jsonText.length() == 4) {
            ServiceLogger.LOGGER.warning("No json text found.");
            requestModel.setInvalidResultCode(-1);
            return requestModel;
        }

        try {
            requestModel = mapper.readValue(jsonText, SCInsertUpdateRequestModel.class);
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

    public static String logRequest(SCInsertUpdateRequestModel requestModel) {
        String log = "Email: " + requestModel.getEmail() + "\n";
        log += "MovieID: " + requestModel.getMovieId() + "\n";
        log += "Quantity: " + requestModel.getQuantity() + "\n";

        return log;
    }
}
