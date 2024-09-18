package com.example.peojulgae;

import java.util.List;

public class Food {
    private String foodId;
    private String name;
    private String description;
    private String price;
    private int discount;
    private int quantity;
    private String imageUrl;
    private String mainImageUrl;  // mainImageUrl 필드 추가
    private List<String> categories; // List to hold categories

    public Food() {
        // Default constructor required for calls to DataSnapshot.getValue(Food.class)
    }

    public Food(String foodId, String name, String description, String price, int discount, int quantity, String imageUrl, String mainImageUrl, List<String> categories) {
        this.foodId = foodId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.mainImageUrl = mainImageUrl;
        this.categories = categories;
    }

    public Food(String foodId, String name, String description, String price, String discount, int i, String imageUrl, List<String> categories) {
    }

    public String getFoodId() {
        return foodId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public List<String> getCategories() {
        return categories;
    }
}
