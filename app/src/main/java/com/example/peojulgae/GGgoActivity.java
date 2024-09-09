package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class GGgoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gggo_display);

        ImageView nicericeImageView = findViewById(R.id.nicerice);
        ImageView noodledoodleImageView = findViewById(R.id.noodledoodle);
        ImageView meatmayoImageView = findViewById(R.id.meatmayo);
        ImageView spicyporkImageView = findViewById(R.id.spicypork);

        // 나이스 라이스 클릭 시 FoodListActivity로 이동
        nicericeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 5000;
                int discountedPrice = calculateDiscount(price);
                Intent intent = new Intent(GGgoActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.nicerice);
                intent.putExtra("food_name", "나이스 라이스");
                intent.putExtra("food_price", price + "원");
                intent.putExtra("discounted_price", discountedPrice + "원");  // 할인된 가격 전달
                startActivity(intent);
            }
        });

        // 누들두들 클릭 시 FoodListActivity로 이동
        noodledoodleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 5000;
                int discountedPrice = calculateDiscount(price);
                Intent intent = new Intent(GGgoActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.noodledoodle);
                intent.putExtra("food_name", "누들두들");
                intent.putExtra("food_price", price + "원");
                intent.putExtra("discounted_price", discountedPrice + "원");  // 할인된 가격 전달
                startActivity(intent);
            }
        });

        // 고기마요 클릭 시 FoodListActivity로 이동
        meatmayoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 5000;
                int discountedPrice = calculateDiscount(price);
                Intent intent = new Intent(GGgoActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.meatmayo);
                intent.putExtra("food_name", "고기마요");
                intent.putExtra("food_price", price + "원");
                intent.putExtra("discounted_price", discountedPrice + "원");  // 할인된 가격 전달
                startActivity(intent);
            }
        });

        // 제육볶음마요 클릭 시 FoodListActivity로 이동
        spicyporkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 5000;
                int discountedPrice = calculateDiscount(price);
                Intent intent = new Intent(GGgoActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.spicypork);
                intent.putExtra("food_name", "제육볶음마요");
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