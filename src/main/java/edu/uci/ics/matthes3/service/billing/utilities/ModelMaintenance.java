package edu.uci.ics.matthes3.service.billing.utilities;

import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;
import edu.uci.ics.matthes3.service.billing.models.*;

public class ModelMaintenance {

    public static class ShoppingCart {
        public static ShoppingCartResponseModel buildResponseModel(int resultCode) {
            String message = setMessage(resultCode);


            ShoppingCartResponseModel shoppingCartResponseModel = new ShoppingCartResponseModel(
                    resultCode,
                    message
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(message);
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(message);

            return shoppingCartResponseModel;
        }

        public static ShoppingCartResponseModel buildItemsResponseModel(int resultCode, ItemModel[] items) {
            String message = setMessage(resultCode);

            ShoppingCartResponseModel shoppingCartResponseModel = new ShoppingCartResponseModel(
                    resultCode,
                    message,
                    items
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(message);
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(message);

            return shoppingCartResponseModel;
        }
    }


    public static class CreditCard {
        public static CreditCardResponseModel buildResponseModel(int resultCode) {
            String message = setMessage(resultCode);

            CreditCardResponseModel creditCardResponseModel = new CreditCardResponseModel(
                    resultCode,
                    message
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(message);
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(message);

            return creditCardResponseModel;
        }

        public static CreditCardResponseModel buildCardResponseModel(int resultCode, CreditCardModel creditCardModel) {
            String message = setMessage(resultCode);

            CreditCardResponseModel creditCardResponseModel = new CreditCardResponseModel(
                    resultCode,
                    message,
                    creditCardModel
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(message);
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(message);

            return creditCardResponseModel;
        }
    }

    public static class Customer {
        public static CustomerResponseModel buildResponseModel(int resultCode) {
            String message = setMessage(resultCode);

            CustomerResponseModel customerResponseModel = new CustomerResponseModel(
                    resultCode,
                    message
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(message);
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(message);

            return customerResponseModel;
        }

        public static CustomerResponseModel buildCustomerResponseModel(int resultCode, CustomerModel customerModel) {
            String message = setMessage(resultCode);

            CustomerResponseModel customerResponseModel = new CustomerResponseModel(
                    resultCode,
                    message,
                    customerModel
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(message);
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(message);

            return customerResponseModel;
        }
    }


    public static class Order {

        public static OrderResponseModel buildResponseModel(int resultCode) {
            String message = setMessage(resultCode);

            OrderResponseModel orderResponseModel = new OrderResponseModel(
                    resultCode,
                    message
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(message);
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(message);

            return orderResponseModel;
        }

        public static OrderResponseModel buildOrderResponseModel(int resultCode, String redirectURL, String token) {
            String message = setMessage(resultCode);

            OrderResponseModel orderResponseModel = new OrderResponseModel(
                    resultCode,
                    message,
                    redirectURL,
                    token
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(message);
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(message);

            return orderResponseModel;
        }

        public static OrderResponseModel buildTransactionResponseModel(int resultCode, TransactionModel[] transactions) {
            String message = setMessage(resultCode);

            OrderResponseModel orderResponseModel = new OrderResponseModel(
                    resultCode,
                    message,
                    transactions
            );

            if (resultCode < -1)
                ServiceLogger.LOGGER.warning(message);
            else if (resultCode > 0)
                ServiceLogger.LOGGER.info(message);

            return orderResponseModel;
        }

    }

    private static String setMessage(int resultCode) {
        if (resultCode == -11)
            return "Email address has invalid format";
        if (resultCode == -10)
            return "Email address has invalid length";
        if (resultCode == -3)
            return "JSON parse Exception.";
        if (resultCode == -2)
            return "JSON Mapping Exception";
        if (resultCode == -1)
            return "Internal server error.";
        if (resultCode == 33)
            return "Quantity has invalid value.";
        if (resultCode == 311)
            return "Duplicate insertion.";
        if (resultCode == 312)
            return "Shopping item does not exist.";
        if (resultCode == 321)
            return "Credit card ID has invalid length.";
        if (resultCode == 322)
            return "Credit card ID has invalid value.";
        if (resultCode == 323)
            return "expiration has invalid value.";
        if (resultCode == 324)
            return "Credit card does not exist.";
        if (resultCode == 325)
            return "Duplicate insertion.";
        if (resultCode == 331)
            return "Credit card ID not found.";
        if (resultCode == 332)
            return "Customer does not exist.";
        if (resultCode == 333)
            return "Duplicate insertion.";
        if (resultCode == 341)
            return "Shopping cart for this customer not found.";
        if (resultCode == 342)
            return "Create payment failed.";
        if (resultCode == 3100)
            return "Shopping cart item inserted successfully";
        if (resultCode == 3110)
            return "Shopping cart item updated successfully.";
        if (resultCode == 3120)
            return "Shopping cart item deleted successfully.";
        if (resultCode == 3130)
            return "Shopping cart retrieved successfully.";
        if (resultCode == 3140)
            return "Shopping cart cleared successfully.";
        if (resultCode == 3200)
            return "Credit card inserted successfully.";
        if (resultCode == 3210)
            return "Credit card updated successfully.";
        if (resultCode == 3220)
            return "Credit card deleted successfully.";
        if (resultCode == 3230)
            return "Credit card retrieved successfully.";
        if (resultCode == 3300)
            return "Customer inserted successfully.";
        if (resultCode == 3310)
            return "Customer updated successfully.";
        if (resultCode == 3320)
            return "Customer retrieved successfully.";
        if (resultCode == 3400)
            return "Order placed successfully";
        if (resultCode == 3410)
            return "Orders retrieved successfully.";
        if (resultCode == 3421)
            return "Token not found.";
        if (resultCode == 3422)
            return "Payment can not be completed.";
        if (resultCode == 3420)
            return "Payment is completed successfully.";

        return "INVALID RESULT CODE";
    }
}
