package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class alchon_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alchon_display);

        ImageView albabImageView = findViewById(R.id.albab);
        ImageView spicyBabImageView = findViewById(R.id.spicy_bab);
        ImageView hotBabImageView = findViewById(R.id.hotbab);
        ImageView veryHotBabImageView = findViewById(R.id.veryhotbab);
        ImageView kimchiBabImageView = findViewById(R.id.kimchibab);

        // 순한 알밥 클릭 시 FoodListActivity로 이동
        albabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 3500;
                int discountedPrice = calculateDiscount(price);
                Intent intent = new Intent(alchon_activity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.albab);
                intent.putExtra("food_name", "순한 알밥");
                intent.putExtra("food_price", price + "원");
                intent.putExtra("discounted_price", discountedPrice + "원");  // 할인된 가격 전달
                startActivity(intent);
            }
        });

        // 약매 알밥 클릭 시 FoodListActivity로 이동
        spicyBabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 3900;
                int discountedPrice = calculateDiscount(price);
                Intent intent = new Intent(alchon_activity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.spicy_bab);
                intent.putExtra("food_name", "약매 알밥");
                intent.putExtra("food_price", price + "원");
                intent.putExtra("discounted_price", discountedPrice + "원");  // 할인된 가격 전달
                startActivity(intent);
            }
        });

        // 매콤 알밥 클릭 시 FoodListActivity로 이동
        hotBabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 3900;
                int discountedPrice = calculateDiscount(price);
                Intent intent = new Intent(alchon_activity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.hot_bab);
                intent.putExtra("food_name", "매콤 알밥");
                intent.putExtra("food_price", price + "원");
                intent.putExtra("discounted_price", discountedPrice + "원");  // 할인된 가격 전달
                startActivity(intent);
            }
        });

        // 진매 알밥 클릭 시 FoodListActivity로 이동
        veryHotBabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 4000;
                int discountedPrice = calculateDiscount(price);
                Intent intent = new Intent(alchon_activity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.veryhotbab);
                intent.putExtra("food_name", "진매 알밥");
                intent.putExtra("food_price", price + "원");
                intent.putExtra("discounted_price", discountedPrice + "원");  // 할인된 가격 전달
                startActivity(intent);
            }
        });

        // 김치볶음밥 클릭 시 FoodListActivity로 이동
        kimchiBabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 7000;
                int discountedPrice = calculateDiscount(price);
                Intent intent = new Intent(alchon_activity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.kimchibab);
                intent.putExtra("food_name", "김치볶음밥");
                intent.putExtra("food_price", price + "원");
                intent.putExtra("discounted_price", discountedPrice + "원");  // 할인된 가격 전달
                startActivity(intent);
            }
        });
    }

    // 10% 할인 계산 함수
    private int calculateDiscount(int price) {
        return price - (price / 10);
    }
}