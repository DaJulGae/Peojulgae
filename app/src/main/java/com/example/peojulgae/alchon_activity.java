package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class alchon_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alchon_display);

        // 할인된 금액 표시
        displayDiscountedPrices();

        ImageView albabImageView = findViewById(R.id.albab);
        ImageView spicyBabImageView = findViewById(R.id.spicy_bab);
        ImageView hotBabImageView = findViewById(R.id.hotbab);
        ImageView veryHotBabImageView = findViewById(R.id.veryhotbab);
        ImageView kimchiBabImageView = findViewById(R.id.kimchibab);

        // 순한 알밥 클릭 시
        albabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFoodListActivity(R.drawable.albab, "순한 알밥", 3500);
            }
        });

        // 약매 알밥 클릭 시
        spicyBabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFoodListActivity(R.drawable.spicy_bab, "약매 알밥", 3900);
            }
        });

        // 매콤 알밥 클릭 시
        hotBabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFoodListActivity(R.drawable.hot_bab, "매콤 알밥", 3900);
            }
        });

        // 진매 알밥 클릭 시
        veryHotBabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFoodListActivity(R.drawable.veryhotbab, "진매 알밥", 4000);
            }
        });

        // 김치볶음밥 클릭 시
        kimchiBabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFoodListActivity(R.drawable.kimchibab, "김치볶음밥", 7000);
            }
        });
    }

    // 10% 할인 계산
    private int calculateDiscount(int price) {
        return price - (price / 10);
    }

    // 할인된 가격 표시
    private void displayDiscountedPrices() {
        updatePriceInView(R.id.store_dtext10, 3500);  // 순한 알밥
        updatePriceInView(R.id.store_dtext16, 3900);  // 약매 알밥
        updatePriceInView(R.id.store_dtext23, 3900);  // 매콤 알밥
        updatePriceInView(R.id.store_dtext29, 4000);  // 진매 알밥
        updatePriceInView(R.id.store_dtext36, 7000);  // 김치볶음밥
    }

    // TextView 업데이트
    private void updatePriceInView(int textViewId, int price) {
        int discountedPrice = calculateDiscount(price);
        TextView priceTextView = findViewById(textViewId);
        priceTextView.setText(discountedPrice + "원");
    }

    // FoodListActivity로 이동
    private void launchFoodListActivity(int imageResource, String foodName, int price) {
        int discountedPrice = calculateDiscount(price);
        Intent intent = new Intent(alchon_activity.this, FoodListActivity.class);
        intent.putExtra("image_resource", imageResource);
        intent.putExtra("food_name", foodName);
        intent.putExtra("food_price", price + "원");
        intent.putExtra("discounted_price", discountedPrice + "원");
        startActivity(intent);
    }
}