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
    private String mainImageUrl;
    private List<String> categories;
    private String userId; // 사용자 ID 필드 추가

    // 기본 생성자
    public Food() {}

    // 모든 필드를 포함한 생성자
    public Food(String foodId, String name, String description, String price, int discount, int quantity, String imageUrl, String mainImageUrl, List<String> categories, String userId) {
        this.foodId = foodId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.mainImageUrl = mainImageUrl;
        this.categories = categories;
        this.userId = userId;
    }

    // Getter 메서드들
    public String getFoodId() { return foodId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public int getDiscount() { return discount; }
    public int getQuantity() { return quantity; }
    public String getImageUrl() { return imageUrl; }
    public String getMainImageUrl() { return mainImageUrl; }
    public List<String> getCategories() { return categories; }
    public String getUserId() { return userId; }

    }
