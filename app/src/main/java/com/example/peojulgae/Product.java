package com.example.peojulgae;

import java.util.List;

public class Product {
    private String productId;
    private String name;
    private String description;
    private String price;
    private Long discount; // discount 필드를 Long 형식으로 변경
    private List<String> photoUrls; // photoUrls 필드 추가

    // 기본 생성자 (Firebase에서 데이터를 가져올 때 필요)
    public Product() {
    }

    public Product(String productId, String name, String description, String price, Long discount, List<String> photoUrls) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.photoUrls = photoUrls;
    }

    // Getter 및 Setter 메서드
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getDiscount() {
        return discount; // 반환 타입을 Long으로 변경
    }

    public void setDiscount(Long discount) { // 파라미터 타입을 Long으로 변경
        this.discount = discount;
    }

    // photoUrls에 대한 Getter 및 Setter 추가
    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }
}
