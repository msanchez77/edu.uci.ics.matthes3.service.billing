package edu.uci.ics.matthes3.service.billing.models;

public class ItemModel {
    private String email;
    private String movieId;
    private String title;
    private int quantity;


    public ItemModel(String email, String movieId, String title, int quantity) {
        this.email = email;
        this.movieId = movieId;
        this.title = title;
        this.quantity = quantity;
    }

    public static ItemModel buildModelFromObject(Item item) {
        return new ItemModel(
                item.getEmail(),
                item.getMovieId(),
                item.getTitle(),
                item.getQuantity()
        );
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

    public int getQuantity() {
        return quantity;
    }
}
