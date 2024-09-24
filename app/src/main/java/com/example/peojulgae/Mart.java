package com.example.peojulgae;

import java.util.List;

public class Mart {
    private String martId;          // 마트의 고유 ID
    private String name;            // 마트 이름
    private String address;         // 마트 주소
    private String phone;           // 마트 전화번호
    private String hours;           // 영업 시간 (예: "10:00 - 21:00")
    private String userId;          // 마트를 등록한 사용자 ID
    private String imageUrl;        // 마트 이미지 URL (선택 사항)
    private String description;     // 마트 소개 (선택 사항)
    private long registrationTime;  // 마트 등록 시간 (타임스탬프)
    private List<String> photoUrls; // 마트 사진 URL 목록

    public Mart() {
    }

    // 필수 필드만 포함한 생성자
    public Mart(String martId, String name, String address, String phone, String hours, String userId) {
        this.martId = martId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.hours = hours;
        this.userId = userId;
    }

    // 모든 필드를 포함한 생성자
    public Mart(String martId, String name, String address, String phone, String hours, String userId, String imageUrl, String description, long registrationTime, List<String> photoUrls) {
        this.martId = martId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.hours = hours;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.description = description;
        this.registrationTime = registrationTime;
        this.photoUrls = photoUrls;  // photoUrls 필드를 초기화
    }

    // Getter 메서드
    public String getMartId() {
        return martId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getHours() {
        return hours;
    }

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public long getRegistrationTime() {
        return registrationTime;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;  // Getter 메서드 추가
    }

    // Setter 메서드
    public void setMartId(String martId) {
        this.martId = martId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegistrationTime(long registrationTime) {
        this.registrationTime = registrationTime;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;  // Setter 메서드 추가
    }

}
