package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class FoodListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);

        // 인텐트에서 이미지 리소스 또는 이미지 URL, 음식 이름, 가격, 할인된 가격을 가져옴
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("image_url"); // 이미지 URL
        int imageResource = intent.getIntExtra("image_resource", -1); // 이미지 리소스 ID, 기본값은 -1
        String foodName = intent.getStringExtra("food_name");
        String description = intent.getStringExtra("description");
        String foodPrice = intent.getStringExtra("food_price");
        String discountedPrice = intent.getStringExtra("discounted_price");

        // ImageView 설정
        ImageView imageView = findViewById(R.id.imageView);

        // 이미지 URL이 있으면 Glide로 로드, 없으면 리소스 ID로 설정
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.store_gana) // 기본 이미지 로드
                    .error(R.drawable.store_gana) // 오류 발생 시 기본 이미지
                    .into(imageView);
        } else if (imageResource != -1) { // 리소스 ID가 유효한 경우에만 로드
            imageView.setImageResource(imageResource);
        }

        // TextView 설정 (음식 이름 및 가격)
        TextView foodNameTextView = findViewById(R.id.food_ltext01);
        foodNameTextView.setText(foodName);

        TextView foodPriceTextView = findViewById(R.id.food_ltext04);
        foodPriceTextView.setText(foodPrice);

        TextView discountedPriceTextView = findViewById(R.id.food_ltext08);
        discountedPriceTextView.setText(discountedPrice);

        TextView descriptionTextView = findViewById(R.id.food_ltext02);
        descriptionTextView.setText(description);

        // 할인된 금액 계산
        int originalPrice = Integer.parseInt(foodPrice.replace("원", "").replace(",", "").trim());
        int discountedPriceValue = Integer.parseInt(discountedPrice.replace("원", "").replace(",", "").trim());

        // food_lbutton04 버튼 찾기
        Button TakePayButton = findViewById(R.id.food_lbutton04);
        Button HavePayButton = findViewById(R.id.food_lbutton05);

        // food_lbutton04 버튼 클릭 리스너 설정 (TakePayActivity로 이동)
        TakePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TakePayActivity로 이동하고 가격 정보 전달
                Intent intent1 = new Intent(FoodListActivity.this, TakePayActivity.class);
                intent1.putExtra("original_price", originalPrice);
                intent1.putExtra("discounted_price", discountedPriceValue);
                startActivity(intent1);
            }
        });

        // HavePayButton 클릭 리스너 설정 (HavePayActivity로 이동)
        HavePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HavePayActivity로 이동하고 가격 정보 전달
                Intent intent2 = new Intent(FoodListActivity.this, HavePayActivity.class);
                intent2.putExtra("original_price", originalPrice);
                intent2.putExtra("discounted_price", discountedPriceValue);
                startActivity(intent2);
            }
        });
    }
}