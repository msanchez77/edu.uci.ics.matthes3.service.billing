package edu.uci.ics.matthes3.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.core.ShoppingCart;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.*;
import edu.uci.ics.matthes3.service.billing.utilities.ModelMaintenance;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;

@Path("cart")
public class ShoppingCartPage {

    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertMovieInShoppingCart(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to insert movie into shopping cart...");
        SCInsertUpdateRequestModel requestModel;
        ShoppingCartResponseModel shoppingCartResponseModel;

        // Attempts to map JSON to Request Model
        requestModel = SCInsertUpdateRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        // Checks if email has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        if (requestModel.getQuantity() < 1) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(33);
            return Response.status(Status.OK).entity(shoppingCartResponseModel).build();
        }

        // Will either return 311 (Duplicate insertion), 3100 (Success), or -1 (SQL Exception)
        int resultCode = ShoppingCart.insert(requestModel);
        shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(resultCode);
        return Response.status(Status.OK).entity(shoppingCartResponseModel).build();
    }


    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateShoppingCart(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to update shopping cart...");
        SCInsertUpdateRequestModel requestModel;
        ShoppingCartResponseModel shoppingCartResponseModel;

        // Attempts to map JSON to Request Model
        requestModel = SCInsertUpdateRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        // Checks if email has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        if (requestModel.getQuantity() < 1) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(33);
            return Response.status(Status.OK).entity(shoppingCartResponseModel).build();
        }

        // Will either return 312 (Shopping cart item does not exist), 3110 (Success), or -1 (SQL Exception)
        int resultCode = ShoppingCart.update(requestModel);
        shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(resultCode);
        return Response.status(Status.OK).entity(shoppingCartResponseModel).build();
    }



    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteShoppingCartItem(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to delete shopping cart item...");
        SCDeleteRequestModel requestModel;
        ShoppingCartResponseModel shoppingCartResponseModel;

        // Attempts to map JSON to Request Model
        requestModel = SCDeleteRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        // Checks if email has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        // Will either return 312 (Shopping cart item does not exist), 3120 (Success), or -1 (SQL Exception)
        int resultCode = ShoppingCart.delete(requestModel);
        shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(resultCode);
        return Response.status(Status.OK).entity(shoppingCartResponseModel).build();
    }


    @POST
    @Path("retrieve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveShoppingCart(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to retrieve shopping cart item...");
        SCRetrieveClearRequestModel requestModel;
        ShoppingCartResponseModel shoppingCartResponseModel;

        // Attempts to map JSON to Request Model
        requestModel = SCRetrieveClearRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        // Checks if email has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        // Will either return 312 (Shopping cart item does not exist), 3130 (Success), or -1 (SQL Exception)
        ItemModel[] items = ShoppingCart.retrieve(requestModel);

        if (items.length != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildItemsResponseModel(3130, items);
            return Response.status(Status.OK).entity(shoppingCartResponseModel).build();
        } else {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(312);
            return Response.status(Status.OK).entity(shoppingCartResponseModel).build();
        }
    }


    @POST
    @Path("clear")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearShoppingCart(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to clear shopping cart...");
        SCRetrieveClearRequestModel requestModel;
        ShoppingCartResponseModel shoppingCartResponseModel;

        // Attempts to map JSON to Request Model
        requestModel = SCRetrieveClearRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        // Checks if email has valid format and length
        requestModel.isDataValid();
        invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(shoppingCartResponseModel).build();
        }

        // Will either return 312 (Shopping cart item does not exist), 3120 (Success), or -1 (SQL Exception)
        int resultCode = ShoppingCart.clear(requestModel);
        shoppingCartResponseModel = ModelMaintenance.ShoppingCart.buildResponseModel(resultCode);
        return Response.status(Status.OK).entity(shoppingCartResponseModel).build();
    }
}
