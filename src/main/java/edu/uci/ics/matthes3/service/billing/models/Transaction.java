package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paypal.api.payments.Currency;
import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;

import java.util.ArrayList;

public class Transaction {
    private String transactionId;
    private String state;
    private Amount amount;
    private TransactionFee transaction_fee;
    private String create_time;
    private String update_time;
    private OrderModel[] items;

    public Transaction(String transactionId, String state, Amount amount,
                       TransactionFee transaction_fee, String create_time,
                       String update_time, OrderModel[] items) {
        this.transactionId = transactionId;
        this.state = state;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_time;
        this.items = items;
    }

    public static TransactionModel[] buildTransactionArray(ArrayList<Transaction> transactions) {
        ServiceLogger.LOGGER.info("Creating TransactionModel from list...");

        if (transactions == null) {
            ServiceLogger.LOGGER.info("No transactions passed to model constructor.");
            return new TransactionModel[0];
        }

        ServiceLogger.LOGGER.info("Transactions list is not empty...");
        int len = transactions.size();
        TransactionModel[] array = new TransactionModel[len];

        for (int i = 0; i < len; ++i) {
//            ServiceLogger.LOGGER.info("Adding " + items.get(i).getEmail() + ", " + items.get(i).getMovieId() + " to array.");
            // Convert each item in the arraylist to a ItemModel
            TransactionModel transactionModel = TransactionModel.buildModelFromObject(transactions.get(i));
            array[i] = transactionModel;
        }

        return array;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getState() {
        return state;
    }

    public Amount getAmount() {
        return amount;
    }

    public TransactionFee getTransaction_fee() {
        return transaction_fee;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public OrderModel[] getItems() {
        return items;
    }
}
