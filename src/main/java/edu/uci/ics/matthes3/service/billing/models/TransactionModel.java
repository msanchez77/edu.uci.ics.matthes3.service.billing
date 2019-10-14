package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paypal.api.payments.Currency;

public class TransactionModel {
    private String transactionId;
    private String state;
    private Amount amount;
    private TransactionFee transaction_fee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ssZ")
    private String create_time;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddTHH:mm:ssZ")
    private String update_time;

    private OrderModel[] items;

    public TransactionModel(String transactionId, String state,
                            Amount amount, TransactionFee transaction_fee,
                            String create_time, String update_time, OrderModel[] items) {
        this.transactionId = transactionId;
        this.state = state;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_time;
        this.items = items;
    }

    public static TransactionModel buildModelFromObject(Transaction transaction) {
        return new TransactionModel(
                transaction.getTransactionId(),
                transaction.getState(),
                transaction.getAmount(),
                transaction.getTransaction_fee(),
                transaction.getCreate_time(),
                transaction.getUpdate_time(),
                transaction.getItems()
        );
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
