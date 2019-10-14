package edu.uci.ics.matthes3.service.billing.core;

import edu.uci.ics.matthes3.service.billing.BillingService;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

public class ShoppingCart {

    public static int insert(SCInsertUpdateRequestModel requestModel) {
        try {
            String email = requestModel.getEmail();
            String movieId = requestModel.getMovieId();
            int quantity = requestModel.getQuantity();

            String query =
                    "INSERT INTO carts (email, movieId, quantity) VALUES (?, ?, ?) ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, email);
            insertPs.setString(2, movieId);
            insertPs.setInt(3, quantity);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            insertPs.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return 3100;

        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                return 311;
            } else {
                e.printStackTrace();
                return -1;
            }
        }
    }

    public static int update(SCInsertUpdateRequestModel requestModel) {
        String email = requestModel.getEmail();
        String movieId = requestModel.getMovieId();
        int quantity = requestModel.getQuantity();

        try {
            String query =
                "UPDATE carts " +
                "SET quantity = ?  " +
                "WHERE email = ? AND movieId = ? ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setInt(1, quantity);
            insertPs.setString(2, email);
            insertPs.setString(3, movieId);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            int result = insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded: " + result);

            return result == 1 ? 3110 : 312;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int delete(SCDeleteRequestModel requestModel) {
        String email = requestModel.getEmail();
        String movieId = requestModel.getMovieId();

        try {
            String query =
                    "DELETE FROM carts " +
                    "WHERE email = ? AND movieId = ? ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, email);
            insertPs.setString(2, movieId);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            int result = insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded: " + result);

            return result == 1 ? 3120 : 312;

        } catch (SQLException e) {
            e.printStackTrace();
            return 312;
        }
    }

    public static ItemModel[] retrieve(SCRetrieveClearRequestModel requestModel) {
        String email = requestModel.getEmail();

        try {
            String query =
                    "SELECT c.email, c.movieId, m.title, c.quantity " +
                    "FROM carts c INNER JOIN movies m ON c.movieId = m.id " +
                    "WHERE email = ? ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            ResultSet rs = insertPs.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded");

            int num_items = 0;
            ArrayList<Item> items = new ArrayList<Item>();
            while (rs.next()) {
                String e = rs.getString("email");
                String m = rs.getString("movieId");
                String t = rs.getString("title");
                int q = rs.getInt("quantity");

                ServiceLogger.LOGGER.info("Adding (" + t + ", " + q + ") to list");
                Item item = new Item(
                        e, m, t, q
                );

                items.add(item);
                num_items++;
            }

            ServiceLogger.LOGGER.info("Retrieved " + num_items + " items.");
            return Item.buildItemArray(items);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ItemModel[0];
        }
    }


    public static int clear(SCRetrieveClearRequestModel requestModel) {
        String email = requestModel.getEmail();

        try {
            String query =
                    "DELETE FROM carts " +
                    "WHERE email = ? ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded");

            return 3140;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
