package edu.uci.ics.matthes3.service.billing.resources;

import edu.uci.ics.matthes3.service.billing.core.CreditCard;
import edu.uci.ics.matthes3.service.billing.core.Customer;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.*;
import edu.uci.ics.matthes3.service.billing.utilities.ModelMaintenance;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.sql.Date;

@Path("customer")
public class CustomerPage {

    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertCustomer(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to insert customer into database...");
        CustomerInsertUpdateRequestModel requestModel;
        CustomerResponseModel responseModel;

        // Attempts to map JSON to Request Model
        requestModel = CustomerInsertUpdateRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.Customer.buildResponseModel(invalidResultCode);
            return Response.status(Response.Status.BAD_REQUEST).entity(responseModel).build();
        }

        // Checks if ccId has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.Customer.buildResponseModel(invalidResultCode);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }

        // Will either return 331 (ccID not found), 325 (Duplicate insertion), 3200 (Success), or -1 (SQL Exception)
        int resultCode = Customer.insert(requestModel);
        responseModel = ModelMaintenance.Customer.buildResponseModel(resultCode);
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }

    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to update customer...");
        CustomerInsertUpdateRequestModel requestModel;
        CustomerResponseModel responseModel;

        // Attempts to map JSON to Request Model
        requestModel = CustomerInsertUpdateRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.Customer.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        // Checks if ccId has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.Customer.buildResponseModel(invalidResultCode);
            return Response.status(Status.OK).entity(responseModel).build();
        }

        // Will either return 331 (Credit card Id does not exist),
        // 332 (Customer does not exist), or 3310 (Success), or -1 (SQL Exception)
        int resultCode = Customer.update(requestModel);
        responseModel = ModelMaintenance.Customer.buildResponseModel(resultCode);
        return Response.status(Response.Status.OK).entity(responseModel).build();
    }


    @POST
    @Path("retrieve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCustomer(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to retrieve customer...");
        CustomerRetrieveRequestModel requestModel;
        CustomerResponseModel responseModel;

        // Attempts to map JSON to Request Model
        requestModel = CustomerRetrieveRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.Customer.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        // Will either return 332 (Customer does not exist), 3320 (Success), or -1 (SQL Exception)
        CustomerModel customer = Customer.retrieve(requestModel);

        if (customer.getEmail() != null) {
            responseModel = ModelMaintenance.Customer.buildCustomerResponseModel(3320, customer);
            return Response.status(Status.OK).entity(responseModel).build();
        } else {
            responseModel = ModelMaintenance.Customer.buildResponseModel(332);
            return Response.status(Status.OK).entity(responseModel).build();
        }
    }
}
