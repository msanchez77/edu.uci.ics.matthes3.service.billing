package edu.uci.ics.matthes3.service.billing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShoppingCartResponseModel extends ResponseModel {
    @JsonIgnore
    private ItemModel[] items;

    public ShoppingCartResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message) {
        super(resultCode, message);
    }

    public ShoppingCartResponseModel(
            @JsonProperty(value = "resultCode", required = true) int resultCode,
            @JsonProperty(value = "message", required = true) String message,
            @JsonProperty("items") ItemModel[] items) {
        super(resultCode, message);
        this.items = items;
    }

    @JsonProperty(value="items")
    public ItemModel[] getItems() {
        return items;
    }

    public void setItems(ItemModel[] items) {
        this.items = items;
    }
}
