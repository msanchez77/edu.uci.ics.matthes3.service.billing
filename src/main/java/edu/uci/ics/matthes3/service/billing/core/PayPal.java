package edu.uci.ics.matthes3.service.billing.core;

import edu.uci.ics.matthes3.service.billing.BillingService;
import edu.uci.ics.matthes3.service.billing.configs.Configs;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.ItemModel;
import edu.uci.ics.matthes3.service.billing.models.OrderCompleteRequestModel;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import javax.ws.rs.core.UriBuilder;

public class PayPal {

    private static String clientId = "AV5V1JDYDja-G6-byBIH_1R_rTitr-gTFImOMT0Q_yY0dlEGTMqIBQrnvHSNfl3DlQfZZseM9Cc5y41b";
    private static String clientSecret = "EO53IwQfOsKLgXX2MADZPPzjQlkSIQ7PtrvZ-1F_7c6q6HxUxvSK45SKj6848IXsDRBRPDAlGLocYYWi";

    public static float initPayment(ItemModel[] movies) {
        float total = 0.0f;
        int num_movies = movies.length;

        try {
            String base_query =
                    "SELECT movieId, unit_price*discount AS price " +
                    "FROM movie_prices " +
                    "WHERE movieId IN (";

            String query = buildQuery(num_movies, base_query);

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);

            for (int i = 0; i < num_movies; i++)
                ps.setString(i + 1, movies[i].getMovieId());

            ResultSet rs = ps.executeQuery();

            int i = 0;
            while (rs.next()) {
                String id = rs.getString("movieId");
                float price = rs.getFloat("price");
                int quantity = movies[i].getQuantity();
                float movie_total = price * quantity;

                ServiceLogger.LOGGER.info("MovieId " + id + " price: " + price);
                ServiceLogger.LOGGER.info("Buying " + quantity + " = " + movie_total);

                total += movie_total;
                i++;
            }

            return total;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static String buildQuery(int num_movies, String base_query) {
        String insert = "?,";
        String insert_all = new String(new char[num_movies]).replace("\0", insert);
        insert_all = insert_all.substring(0, insert_all.length() - 1);

        String query = base_query + insert_all + ") ";

        return query;
    }

    public static Map<String, Object> createPayment(float sale_total) {
        Map<String, Object> response = new HashMap<String, Object>();

        String sum = roundFloat(sale_total);

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(sum);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        Configs configs = BillingService.getConfigs();
        String scheme = configs.getScheme();
        String hostName = configs.getHostName();
        String path = configs.getPath();
        int port = configs.getPort();
        URI uri = UriBuilder.fromUri(scheme + hostName + path).port(port).build();

        // frontend page redirect
//        "http://andromeda-70.ics.uci.edu:xxxx/<wherever you want to redirect the customer to>.html"
        URI uriFrontEnd = UriBuilder.fromUri(scheme + hostName + "").port(8301).build();

        RedirectUrls redirectUrls = new RedirectUrls();
        // TODO: Set Cancel URL ???
        redirectUrls.setCancelUrl(uri.toString() + "/order/cancel");
        redirectUrls.setReturnUrl(uri.toString() + "/order/complete");
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        try {
            String redirectUrl = "";
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            createdPayment = payment.create(context);
            if(createdPayment!=null){
                List<Links> links = createdPayment.getLinks();
                for (Links link:links) {
                    if(link.getRel().equals("approval_url")){
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            System.out.println("Error happened during payment creation!");
            response.put("status", "failure");
        }
        return response;
    }

    public static Map<String, Object> completePayment(OrderCompleteRequestModel requestModel){
        Map<String, Object> response = new HashMap();
        Payment payment = new Payment();
        payment.setId(requestModel.getPaymentId());

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(requestModel.getPayerID());
        try {
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(context, paymentExecution);
            if(createdPayment!=null){
                response.put("status", "success");
                response.put("payment", createdPayment);

                String transactionId = createdPayment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
                response.put("transactionId", transactionId);
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
            response.put("status", "failure");
        }
        return response;
    }

    private static String roundFloat(float sale_total) {
        BigDecimal bd = new BigDecimal(sale_total);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return Float.toString(bd.floatValue());
    }

    public static Sale getSale(String id) {
        ServiceLogger.LOGGER.info("Inside getSale...");
        try {
            APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
            return Sale.get(apiContext, id);
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
            return null;
        }
    }
}
