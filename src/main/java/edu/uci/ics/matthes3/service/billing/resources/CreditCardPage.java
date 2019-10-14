package edu.uci.ics.matthes3.service.billing.resources;

import edu.uci.ics.matthes3.service.billing.core.CreditCard;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.*;
import edu.uci.ics.matthes3.service.billing.utilities.ModelMaintenance;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.sql.Date;

@Path("creditcard")
public class CreditCardPage {

    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCreditCard(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to insert credit card into database...");
        CCInsertUpdateRequestModel requestModel;
        CreditCardResponseModel responseModel;

        // Attempts to map JSON to Request Model
        requestModel = CCInsertUpdateRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        // Checks if ccId has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(invalidResultCode);
            return Response.status(Status.OK).entity(responseModel).build();
        }

        Date today = new Date(System.currentTimeMillis());
        if (requestModel.getExpiration().before(today)) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(323);
            return Response.status(Status.OK).entity(responseModel).build();
        }

        // Will either return 325 (Duplicate insertion), 3200 (Success), or -1 (SQL Exception)
        int resultCode = CreditCard.insert(requestModel);
        responseModel = ModelMaintenance.CreditCard.buildResponseModel(resultCode);
        return Response.status(Status.OK).entity(responseModel).build();
    }


    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCreditCard(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to update credit card...");
        CCInsertUpdateRequestModel requestModel;
        CreditCardResponseModel responseModel;

        // Attempts to map JSON to Request Model
        requestModel = CCInsertUpdateRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        // Checks if ccId has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(invalidResultCode);
            return Response.status(Status.OK).entity(responseModel).build();
        }

        Date today = new Date(System.currentTimeMillis());
        if (requestModel.getExpiration().before(today)) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(323);
            return Response.status(Status.OK).entity(responseModel).build();
        }

        // Will either return 324 (Credit card does not exist), 3210 (Success), or -1 (SQL Exception)
        int resultCode = CreditCard.update(requestModel);
        responseModel = ModelMaintenance.CreditCard.buildResponseModel(resultCode);
        return Response.status(Status.OK).entity(responseModel).build();
    }


    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCreditCard(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to delete credit card...");
        CCDeleteRetrieveRequestModel requestModel;
        CreditCardResponseModel responseModel;

        // Attempts to map JSON to Request Model
        requestModel = CCDeleteRetrieveRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        // Checks if id has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(invalidResultCode);
            return Response.status(Status.OK).entity(responseModel).build();
        }

        // Will either return 324 (Credit card does not exist), 3230 (Success), or -1 (SQL Exception)
        int resultCode = CreditCard.delete(requestModel);
        responseModel = ModelMaintenance.CreditCard.buildResponseModel(resultCode);
        return Response.status(Status.OK).entity(responseModel).build();
    }


    @POST
    @Path("retrieve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCreditCard(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to retrieve credit card...");
        CCDeleteRetrieveRequestModel requestModel;
        CreditCardResponseModel responseModel;

        // Attempts to map JSON to Request Model
        requestModel = CCDeleteRetrieveRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        // Checks if id has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(invalidResultCode);
            return Response.status(Status.OK).entity(responseModel).build();
        }

        // Will either return 324 (Credit card does not exist), 3230 (Success), or -1 (SQL Exception)
        CreditCardModel creditCard = CreditCard.retrieve(requestModel);

        if (creditCard.getId() != null) {
            responseModel = ModelMaintenance.CreditCard.buildCardResponseModel(3230, creditCard);
            return Response.status(Status.OK).entity(responseModel).build();
        } else {
            responseModel = ModelMaintenance.CreditCard.buildResponseModel(324);
            return Response.status(Status.OK).entity(responseModel).build();
        }
    }
}
