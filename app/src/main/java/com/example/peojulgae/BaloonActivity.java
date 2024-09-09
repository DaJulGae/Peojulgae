package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BaloonActivity extends AppCompatActivity { // 가나 점보 돈까스
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_display);

        // 미리 결제금액 표시
        displayDiscountedPrices();

        ImageView normalDonggasImageView = findViewById(R.id.normaldonggas);
        ImageView cheeseDonggasImageView = findViewById(R.id.cheesedonggas);
        ImageView eggDonggasImageView = findViewById(R.id.eggdongas);
        ImageView eggCheeseDonggasImageView = findViewById(R.id.eggcheese);
        ImageView MulnaengmyeonImageView = findViewById(R.id.Mulnaengmyeon);

        // 가나 점보 돈까스 클릭 시
        normalDonggasImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 8000;
                int discountedPrice = calculateDiscount(price);
                launchFoodListActivity(R.drawable.store_gana, "가나점보 돈까스", price, discountedPrice);
            }
        });

        // 고구마 치즈 돈까스 클릭 시
        cheeseDonggasImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 8500;
                int discountedPrice = calculateDiscount(price);
                launchFoodListActivity(R.drawable.cheese, "고구마 치즈 돈까스", price, discountedPrice);
            }
        });

        // egg + 가나 점보 돈까스 클릭 시
        eggDonggasImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 8500;
                int discountedPrice = calculateDiscount(price);
                launchFoodListActivity(R.drawable.egg_ganajumbo, "egg + 가나 점보 돈까스", price, discountedPrice);
            }
        });

        // egg + 치즈 돈까스 클릭 시
        eggCheeseDonggasImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 8500;
                int discountedPrice = calculateDiscount(price);
                launchFoodListActivity(R.drawable.cheese2, "egg + 치즈 돈까스", price, discountedPrice);
            }
        });

        // 물냉면 클릭 시
        MulnaengmyeonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = 7500;
                int discountedPrice = calculateDiscount(price);
                launchFoodListActivity(R.drawable.weter, "물냉면", price, discountedPrice);
            }
        });
    }

    // 10% 할인 계산 함수
    private int calculateDiscount(int price) {
        return price - (price / 10);
    }

    // 결제 금액을 TextView에 표시하는 함수
    private void displayDiscountedPrices() {
        updatePriceInView(R.id.store_dtext10, calculateDiscount(8000));  // 가나 점보 돈까스
        updatePriceInView(R.id.store_dtext16, calculateDiscount(8500));  // 고구마 치즈 돈까스
        updatePriceInView(R.id.store_dtext23, calculateDiscount(8500));  // egg + 가나 점보 돈까스
        updatePriceInView(R.id.store_dtext29, calculateDiscount(8500));  // egg + 치즈 돈까스
        updatePriceInView(R.id.store_dtext36, calculateDiscount(7500));  // 물냉면
    }

    // 결제 금액을 해당 TextView에 업데이트하는 함수
    private void updatePriceInView(int textViewId, int discountedPrice) {
        TextView priceTextView = findViewById(textViewId);
        priceTextView.setText(discountedPrice + "원");
    }

    // FoodListActivity로 이동하는 함수
    private void launchFoodListActivity(int imageResource, String foodName, int price, int discountedPrice) {
        Intent intent = new Intent(BaloonActivity.this, FoodListActivity.class);
        intent.putExtra("image_resource", imageResource);
        intent.putExtra("food_name", foodName);
        intent.putExtra("food_price", price + "원");
        intent.putExtra("discounted_price", discountedPrice + "원");
        startActivity(intent);
    }
}