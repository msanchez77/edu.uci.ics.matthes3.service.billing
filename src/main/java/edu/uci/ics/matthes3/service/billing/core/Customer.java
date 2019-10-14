package edu.uci.ics.matthes3.service.billing.core;

import edu.uci.ics.matthes3.service.billing.BillingService;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.*;

import java.sql.*;

public class Customer {

    public static int insert(CustomerInsertUpdateRequestModel requestModel) {
        try {
            String email = requestModel.getEmail();
            String firstName = requestModel.getFirstName();
            String lastName = requestModel.getLastName();
            String ccId = requestModel.getCcId();
            String address = requestModel.getAddress();

            String query =
                    "INSERT INTO customers (email, firstName, lastName, ccId, address) VALUES (?, ?, ?, ?, ?) ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, email);
            insertPs.setString(2, firstName);
            insertPs.setString(3, lastName);
            insertPs.setString(4, ccId);
            insertPs.setString(5, address);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return 3300;

        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                int result = e.getErrorCode();
                return result == 1452 ? 331 : 333;
            } else {
                e.printStackTrace();
                return -1;
            }
        }
    }

    public static int update(CustomerInsertUpdateRequestModel requestModel) {
        try {
            String email = requestModel.getEmail();
            String firstName = requestModel.getFirstName();
            String lastName = requestModel.getLastName();
            String ccId = requestModel.getCcId();
            String address = requestModel.getAddress();

            String query =
                    "UPDATE customers " +
                    "SET firstName = ?, lastName = ?, ccId = ?, address = ? " +
                    "WHERE email = ? ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, firstName);
            insertPs.setString(2, lastName);
            insertPs.setString(3, ccId);
            insertPs.setString(4, address);
            insertPs.setString(5, email);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            int result = insertPs.executeUpdate();
            ServiceLogger.LOGGER.info("Query succeeded.");

            return result == 1 ? 3310 : 332;

        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException) {
                return 331;
            } else {
                e.printStackTrace();
                return -1;
            }
        }
    }

    public static CustomerModel retrieve(CustomerRetrieveRequestModel requestModel) {
        String email = requestModel.getEmail();

        try {
            String query =
                    "SELECT email, firstName, lastName, ccId, address " +
                    "FROM customers " +
                    "WHERE email = ? ";

            PreparedStatement insertPs = BillingService.getCon().prepareStatement(query);

            insertPs.setString(1, email);

            ServiceLogger.LOGGER.info("Trying query: " + insertPs.toString());
            ResultSet rs = insertPs.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded");

            if (rs.next()) {
                return new CustomerModel(
                        rs.getString("email"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("ccId"),
                        rs.getString("address")
                );
            } else
                return new CustomerModel();

        } catch (SQLException e) {
            e.printStackTrace();
            return new CustomerModel();
        }
    }
}
