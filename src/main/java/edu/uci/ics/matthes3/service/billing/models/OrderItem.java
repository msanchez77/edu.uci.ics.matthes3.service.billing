package edu.uci.ics.matthes3.service.billing.models;

import edu.uci.ics.matthes3.service.billing.logger.ServiceLogger;

import java.sql.Date;
import java.util.ArrayList;

public class OrderItem {
    private String email;
    private String movieId;
    private String title;
    private Integer quantity;
    private Float unit_price;
    private Float discount;
    private Date saleDate;

    public OrderItem(String email, String movieId, String title, Integer quantity, Float unit_price, Float discount, Date saleDate) {
        this.email = email;
        this.movieId = movieId;
        this.title = title;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.discount = discount;
        this.saleDate = saleDate;
    }

    public static OrderModel[] buildOrderArray(ArrayList<OrderItem> orders) {
        ServiceLogger.LOGGER.info("Creating OrderModel from list...");

        if (orders == null) {
            ServiceLogger.LOGGER.info("No orders passed to model constructor.");
            return new OrderModel[0];
        }

        ServiceLogger.LOGGER.info("Orders list is not empty...");
        int len = orders.size();
        OrderModel[] array = new OrderModel[len];

        for (int i = 0; i < len; ++i) {
//            ServiceLogger.LOGGER.info("Adding " + items.get(i).getEmail() + ", " + items.get(i).getMovieId() + " to array.");
            // Convert each item in the arraylist to a ItemModel
            OrderModel orderModel = OrderModel.buildModelFromObject(orders.get(i));
            array[i] = orderModel;
        }

        return array;
    }

    public String getEmail() {
        return email;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Float getUnit_price() {
        return unit_price;
    }

    public Float getDiscount() {
        return discount;
    }

    public Date getSaleDate() {
        return saleDate;
    }
}
