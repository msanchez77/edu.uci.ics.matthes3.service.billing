package edu.uci.ics.matthes3.service.billing.resources;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.matthes3.service.billing.core.Order;
import edu.uci.ics.matthes3.service.billing.core.PayPal;
import edu.uci.ics.matthes3.service.billing.core.ShoppingCart;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.*;
import edu.uci.ics.matthes3.service.billing.utilities.ModelMaintenance;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.Map;

@Path("order")
public class OrderPage {

    @POST
    @Path("place")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response placeOrder(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to place an order...");
        OrderRequestModel requestModel;
        OrderResponseModel responseModel;

        // Attempts to map JSON to Request Model
        requestModel = OrderRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.Order.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }


        ItemModel[] items = Order.retrieveCustomerShoppingCart(requestModel.getEmail());
        // Customer does not exist.
        if (items == null) {
            responseModel = ModelMaintenance.Order.buildResponseModel(332);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }
        // Shopping cart for this customer not found.
        if (items.length == 0) {
            responseModel = ModelMaintenance.Order.buildResponseModel(341);
            return Response.status(Response.Status.OK).entity(responseModel).build();
        }

        // PayPal information
        float sale_total = PayPal.initPayment(items);
        Map<String, Object> redirectURL_map = PayPal.createPayment(sale_total);
        ServiceLogger.LOGGER.info("Return by PayPal: " + redirectURL_map.toString());

        if (redirectURL_map.get("status").equals("failure")) {
            responseModel = ModelMaintenance.Order.buildResponseModel(342);
        }
        else {
            String redirectURL = redirectURL_map.get("redirect_url").toString();
            ServiceLogger.LOGGER.info("Sale total: " + sale_total);
            ServiceLogger.LOGGER.info("Redirect URL: " + redirectURL);

            int resultCode = Order.insertSalesTransactions(requestModel, items, redirectURL);

            if (resultCode != 3400) {
                responseModel = ModelMaintenance.Order.buildResponseModel(342);
            } else {
                String token = redirectURL.substring(redirectURL.lastIndexOf('=') + 1);

                responseModel = ModelMaintenance.Order.buildOrderResponseModel(3400, redirectURL, token);

                // TODO: Uncomment after testing
                SCRetrieveClearRequestModel clearRequestModel = new SCRetrieveClearRequestModel(requestModel.getEmail());
                ShoppingCart.clear(clearRequestModel);
            }
        }


        return Response.status(Response.Status.OK).entity(responseModel).build();
    }


    @GET
    @Path("complete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeOrder(
            @QueryParam("paymentId") String paymentId,
            @QueryParam("token") String token,
            @QueryParam("PayerID") String PayerID) {
        ServiceLogger.LOGGER.info("Getting request to complete order...");
        OrderCompleteRequestModel requestModel;
        OrderResponseModel responseModel;
        ObjectMapper mapper = new ObjectMapper();

        String jsonText = buildJsonText(paymentId, token, PayerID);

        try {
            JsonNode jsonNode = mapper.readTree(jsonText);
            requestModel = mapper.treeToValue(jsonNode, OrderCompleteRequestModel.class);

            Map<String, Object> response = PayPal.completePayment(requestModel);
            if (response.get("status").equals("failure")) {
                responseModel = ModelMaintenance.Order.buildResponseModel(3422);
                return Response.status(Status.OK).entity(responseModel).build();
            }

            String transacationId = response.get("transactionId").toString();

            int resultCode = Order.complete(requestModel, transacationId);

            responseModel = ModelMaintenance.Order.buildResponseModel(resultCode);
            return Response.status(Status.OK).entity(responseModel).build();
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof JsonParseException) {
                ServiceLogger.LOGGER.warning("JsonParseException");
            } else if (e instanceof JsonMappingException) {
                ServiceLogger.LOGGER.warning("JsonMappingException");
            } else {
                ServiceLogger.LOGGER.warning("IOException");
            }
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    private String buildJsonText(String paymentId, String token, String PayerID) {
        return String.format("{\"paymentId\":\"%s\",\"token\":\"%s\",\"PayerID\":\"%s\"}", paymentId, token, PayerID);
    }


    @GET
    @Path("cancel")
    public Response cancelOrder() {
        ServiceLogger.LOGGER.info("Getting request to retrieve order...");
        return Response.status(Status.OK).build();
    }


    @POST
    @Path("retrieve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveOrder(String jsonText) {
        ServiceLogger.LOGGER.info("Getting request to retrieve order...");
        OrderRequestModel requestModel;
        OrderResponseModel responseModel;

        // Attempts to map JSON to Request Model
        requestModel = OrderRequestModel.isRequestValid(jsonText);
        int invalidResultCode = requestModel.getInvalidResultCode();
        if (invalidResultCode != 0) {
            responseModel = ModelMaintenance.Order.buildResponseModel(invalidResultCode);
            return Response.status(Status.BAD_REQUEST).entity(responseModel).build();
        }

        // Will either return an empty array (Customer does not exist), or an array of transactions 3410 (Success)
        TransactionModel[] transactions = Order.retrieve(requestModel);

        if (transactions.length != 0)
            responseModel = ModelMaintenance.Order.buildTransactionResponseModel(3410, transactions);
        else
            responseModel = ModelMaintenance.Order.buildResponseModel(332);

        return Response.status(Status.OK).entity(responseModel).build();
    }

}
