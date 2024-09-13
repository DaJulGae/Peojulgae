package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;

public class FoodCategoryActivity extends AppCompatActivity {
    ImageView ImageView1, ImageView2, ImageView3, ImageView4, ImageView5, ImageView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_category);

        ImageView1 = findViewById(R.id.peachimage);
        ImageView2 = findViewById(R.id.honeyapple);
        ImageView3 = findViewById(R.id.watermelonimage);
        ImageView4 = findViewById(R.id.kiwiimage);
        ImageView5 = findViewById(R.id.shinemuscatimage);
        ImageView6 = findViewById(R.id.mangoimage);

        TabLayout tabLayout = findViewById(R.id.food_tab_layout);

        // Add Tab Selection Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "야채":
                        updateVegetableImages();
                        break;
                    case "과일":
                        updateFruitImages();
                        break;
                    case "육류":
                        updateMeatImages(); // 육류 탭 선택 시 이미지 변경
                        break;
                    case "기타":
                        updateElsesImages(); // 기타 탭 선택 시 이미지 변경
                        break;
                    // Add other cases for different categories
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        setupImageViewClickListeners();
    }

    private void updateFruitImages() {
        ImageView1.setImageResource(R.drawable.peach);
        ImageView2.setImageResource(R.drawable.apple);
        ImageView3.setImageResource(R.drawable.watermelon);
        ImageView4.setImageResource(R.drawable.kiwi);
        ImageView5.setImageResource(R.drawable.shine_muscat);
        ImageView6.setImageResource(R.drawable.mango);
    }

    private void updateVegetableImages() {
        ImageView1.setImageResource(R.drawable.carrot); // Example vegetable image
        ImageView2.setImageResource(R.drawable.carrot);
        ImageView3.setImageResource(R.drawable.carrot);
        ImageView4.setImageResource(R.drawable.carrot);
        ImageView5.setImageResource(R.drawable.carrot);
        ImageView6.setImageResource(R.drawable.carrot);
    }

    private void updateMeatImages() {
        ImageView1.setImageResource(R.drawable.meat_cow); // 고기 이미지로 변경
        ImageView2.setImageResource(R.drawable.meat_cow);
        ImageView3.setImageResource(R.drawable.meat_cow);
        ImageView4.setImageResource(R.drawable.meat_cow);
        ImageView5.setImageResource(R.drawable.meat_cow);
        ImageView6.setImageResource(R.drawable.meat_cow);
    }

    private void updateElsesImages() {
        ImageView1.setImageResource(R.drawable.mealkit); // 고기 이미지로 변경
        ImageView2.setImageResource(R.drawable.mealkit);
        ImageView3.setImageResource(R.drawable.mealkit);
        ImageView4.setImageResource(R.drawable.mealkit);
        ImageView5.setImageResource(R.drawable.mealkit);
        ImageView6.setImageResource(R.drawable.mealkit);
    }

    private void setupImageViewClickListeners() {
        ImageView1.setOnClickListener(v -> openMartSpecificActivity(R.drawable.peach, "납작 복숭아", "34,120원"));
        ImageView2.setOnClickListener(v -> openMartSpecificActivity(R.drawable.apple, "꿀 사과", "24,320원"));
        ImageView3.setOnClickListener(v -> openMartSpecificActivity(R.drawable.watermelon, "수박", "40,000원"));
        ImageView4.setOnClickListener(v -> openMartSpecificActivity(R.drawable.kiwi, "키위", "25,000원"));
        ImageView5.setOnClickListener(v -> openMartSpecificActivity(R.drawable.shine_muscat, "샤인머스캣", "40,000원"));
        ImageView6.setOnClickListener(v -> openMartSpecificActivity(R.drawable.mango, "망고", "24,320원"));

    }

    private void openMartSpecificActivity(int imageResource, String itemName, String itemPrice) {
        Intent intent = new Intent(FoodCategoryActivity.this, MartSpecific.class);
        intent.putExtra("image_resource", imageResource);
        intent.putExtra("item_name", itemName);
        intent.putExtra("item_price", itemPrice);
        startActivity(intent);
    }
}