package edu.uci.ics.matthes3.service.billing.core;

import edu.uci.ics.matthes3.service.billing.BillingService;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.*;

import java.sql.*;

public class CreditCard {

    public static int insert(CCInsertUpdateRequestModel requestModel) {
        try {
            String id = requestModel.getId();
            String firstName = requestModel.getFirstName();
            String lastName = requestModel.getLastName();
            Date expiration = requestModel.getExpiration();

            String query =
                    "INSERT INTO creditcards (id, firstName, lastName, expiration) VALUES (?, ?, ?, ?) ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, id);
            insertPs.setString(2, firstName);
            insertPs.setString(3, lastName);
            insertPs.setDate(4, expiration);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            insertPs.execute();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return 3200;

        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                return 325;
            } else {
                e.printStackTrace();
                return -1;
            }
        }
    }


    public static int update(CCInsertUpdateRequestModel requestModel) {
        String id = requestModel.getId();
        String firstName = requestModel.getFirstName();
        String lastName = requestModel.getLastName();
        Date expiration = requestModel.getExpiration();

        try {
            String query =
                    "UPDATE creditcards " +
                    "SET firstName = ?, lastName = ?, expiration = ? " +
                    "WHERE id = ? ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, firstName);
            insertPs.setString(2, lastName);
            insertPs.setDate(3, expiration);
            insertPs.setString(4, id);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            int result = insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded: " + result);

            return result == 1 ? 3210 : 324;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static int delete(CCDeleteRetrieveRequestModel requestModel) {
        String id = requestModel.getId();

        try {
            String query =
                    "DELETE FROM creditcards " +
                    "WHERE id = ? ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, id);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            int result = insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded: " + result);

            return result == 1 ? 3220 : 324;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static CreditCardModel retrieve(CCDeleteRetrieveRequestModel requestModel) {
        String id = requestModel.getId();

        try {
            String query =
                    "SELECT id, firstName, lastName, expiration " +
                    "FROM creditcards " +
                    "WHERE id = ? ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, id);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            ResultSet rs = insertPs.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded");

            if (rs.next()) {
                return new CreditCardModel(
                        rs.getString("id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getDate("expiration")
                );
            } else
                return new CreditCardModel();

        } catch (SQLException e) {
            e.printStackTrace();
            return new CreditCardModel();
        }
    }

}
