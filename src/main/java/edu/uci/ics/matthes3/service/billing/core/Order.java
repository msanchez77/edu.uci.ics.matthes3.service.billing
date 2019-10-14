package edu.uci.ics.matthes3.service.billing.core;

import com.paypal.api.payments.Sale;
import edu.uci.ics.matthes3.service.billing.BillingService;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.*;
import edu.uci.ics.matthes3.service.billing.utilities.ArrayConversion;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class Order {

    public static ItemModel[] retrieveCustomerShoppingCart(String email) {
        try {
            String query =
                    "SELECT customers.email, group_concat(carts.movieId) AS movieId, group_concat(m.title) AS title, group_concat(quantity) AS quantity " +
                    "FROM customers LEFT OUTER JOIN carts ON customers.email = carts.email " +
                            "INNER JOIN movies m on m.id = carts.movieId " +
                    "WHERE customers.email = ? " +
                    "GROUP BY customers.email ";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);

            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded");

            ArrayList<Item> items = new ArrayList<Item>();

            if (rs.next())  {
                String e = rs.getString("email");

                String[] movieIds = ArrayConversion.buildArrayFromSQL(rs, "movieId");
                String[] titles = ArrayConversion.buildArrayFromSQL(rs, "title");
                String[] quantities = ArrayConversion.buildArrayFromSQL(rs, "quantity");

                ServiceLogger.LOGGER.info("Found movieIds: " + movieIds);
                ServiceLogger.LOGGER.info("Found titles: " + titles);
                ServiceLogger.LOGGER.info("Found quantities: " + quantities);

                for (int i = 0; i < movieIds.length; i++) {
                    ServiceLogger.LOGGER.info("In for loop..");

                    String m = movieIds[i];
                    String t = titles[i];
                    int q = Integer.parseInt(quantities[i]);

                    ServiceLogger.LOGGER.info("Adding (" + m + ", " + t + ", " +  q + ") to list");
                    Item item = new Item(
                            e, m, t, q
                    );

                    items.add(item);
                }


            } else {
                ServiceLogger.LOGGER.info("User not found");
                return null;
            }

            ServiceLogger.LOGGER.info("Exiting retrieveCustomerShoppingCart with " + items.size() + " items.");
            return Item.buildItemArray(items);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TransactionModel[] retrieve(OrderRequestModel requestModel) {
        String email = requestModel.getEmail();

        try {
            String query =
                    "SELECT transactionId, s.email, group_concat(s.movieId) AS movieIds, group_concat(m.title) AS titles, " +
                            "group_concat(s.quantity) AS quantities, group_concat(mp.unit_price) AS prices, " +
                            "group_concat(mp.discount) AS discounts, group_concat(s.saleDate) AS dates " +
                    "FROM sales s " +
                        "LEFT OUTER JOIN transactions t on s.id = t.sId " +
                        "LEFT OUTER JOIN movie_prices mp on s.movieId = mp.movieId " +
                        "INNER JOIN movies m on m.id = mp.movieId " +
                    "WHERE email = ? " +
                    "GROUP BY transactionId ";

            PreparedStatement ps = BillingService.getCon().prepareStatement(query);

            ps.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + ps.toString());
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded");

            int num_items = 0;
            ArrayList<Transaction> transactions = new ArrayList<Transaction>();
            while (rs.next()) {
                String id = rs.getString("transactionId");

                Sale sale = PayPal.getSale(id);

                if (sale == null) {
                    ServiceLogger.LOGGER.info(id + " returned null");
                } else {
                    String[] movieIds = ArrayConversion.buildArrayFromSQL(rs, "movieIds");
                    String[] titles = ArrayConversion.buildArrayFromSQL(rs, "titles");
                    Integer[] quantities = ArrayConversion.integer(ArrayConversion.buildArrayFromSQL(rs, "quantities"));
                    Float[] prices = ArrayConversion.floatArr(ArrayConversion.buildArrayFromSQL(rs, "prices"));
                    Float[] discounts = ArrayConversion.floatArr(ArrayConversion.buildArrayFromSQL(rs, "discounts"));
                    Date[] saleDates = ArrayConversion.date(ArrayConversion.buildArrayFromSQL(rs, "dates"));

                    int num_orders = movieIds.length;

                    OrderModel[] orders = new OrderModel[num_orders];
                    for (int i = 0; i < num_orders; i++) {
                        orders[i] = new OrderModel(
                                rs.getString("email"),
                                movieIds[i],
                                titles[i],
                                quantities[i],
                                prices[i],
                                discounts[i],
                                saleDates[i]
                        );
                    }

                    Amount amount = new Amount(
                            sale.getAmount().getTotal(), sale.getAmount().getCurrency());
                    TransactionFee transactionFee = new TransactionFee(
                            sale.getTransactionFee().getValue(), sale.getTransactionFee().getCurrency());

                    Transaction transaction = new Transaction(
                            id,
                            sale.getState(),
                            amount,
                            transactionFee,
                            sale.getCreateTime(),
                            sale.getUpdateTime(),
                            orders
                    );

                    transactions.add(transaction);
                    num_items++;
                }

            }

            ServiceLogger.LOGGER.info("Retrieved " + num_items + " items.");
            return Transaction.buildTransactionArray(transactions);

        } catch (SQLException e) {
            e.printStackTrace();
            return new TransactionModel[0];
        }
    }


    public static int insertSalesTransactions(OrderRequestModel requestModel, ItemModel[] items_in_cart, String redirectURL) {

        try {
            Date today = new Date(System.currentTimeMillis());
            String token = redirectURL.substring(redirectURL.lastIndexOf('=') + 1);

            for (ItemModel item : items_in_cart) {
                String procedureQuery =
                        "{CALL insert_sales_transactions (?, ?, ?, ?, ?)}";

                CallableStatement cs = BillingService.getCon().prepareCall(procedureQuery);

                cs.setString(1, item.getEmail());
                cs.setString(2, item.getMovieId());
                cs.setInt(3, item.getQuantity());
                cs.setDate(4, today);
                cs.setString(5, token);

                ServiceLogger.LOGGER.info("Trying query: " + cs.toString());
                cs.execute();
                ServiceLogger.LOGGER.info("Query succeeded");
            }

            return 3400;
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Error in insertSalesTransactions");
            e.printStackTrace();
            return 342;
        }
    }

    public static int complete(OrderCompleteRequestModel requestModel, String transactionId) {
        try {
            String update_query =
                    "UPDATE transactions " +
                    "SET transactionId = ? " +
                    "WHERE token = ? ";

            PreparedStatement update_ps = BillingService.getCon().prepareStatement(update_query);

            update_ps.setString(1, transactionId);
            update_ps.setString(2, requestModel.getToken());

            ServiceLogger.LOGGER.info("Trying query: " + update_ps.toString());
            int result = update_ps.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.: " + result);

            return result != 0 ? 3420 : 3421;
        } catch (SQLException e) {
            e.printStackTrace();
            return 3421;
        }
    }
}



//    public static ItemModel[] place(OrderRequestModel requestModel) {
//        try {
//            String email = requestModel.getEmail();
//            Date today = new Date(System.currentTimeMillis());
//
//            ServiceLogger.LOGGER.info("1");
//            ItemModel[] items_in_cart = retrieveCustomerShoppingCart(email);
//            ServiceLogger.LOGGER.info("2");
//            // Customer does not exist.
//            if (items_in_cart == null)
//                return items_in_cart;
//            // Shopping cart for this customer not found.
//            if (items_in_cart.length == 0)
//                return items_in_cart;
//
//            String query =
//                    "INSERT INTO sales (email, movieId, quantity, saleDate) VALUES (?, ?, ?, ?) ";
//
//            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);
//
//            for (ItemModel item : items_in_cart) {
//                String m = item.getMovieId();
//                int q = item.getQuantity();
//
//                insertPs.setString(1, email);
//                insertPs.setString(2, m);
//                insertPs.setInt(3, q);
//                insertPs.setDate(4, today);
//                insertPs.addBatch();
//            }
//
//            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
//            int[] numUpdates = insertPs.executeBatch();
//            ServiceLogger.LOGGER.info("Query succeeded: Ordered " + numUpdates.length + " movies.");
//
//            return items_in_cart;
//
//        } catch (SQLException e) {
//            if (e instanceof SQLIntegrityConstraintViolationException) {
//                return null;
//            } else {
//                e.printStackTrace();
//                return null;
//            }
//        }
//    }