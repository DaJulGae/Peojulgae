package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;

public class FoodCategoryActivity extends AppCompatActivity {
    ImageView ImageView1, ImageView2, ImageView3, ImageView4, ImageView5, ImageView6;
    TextView textView1, textView2, textView3, textView4, textView5, textView6,textView7,textView8,textView9,textView10,textView11,textView12;

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

        // TextView 초기화 (상품 설명)
        textView1 = findViewById(R.id.food_ctext02);
        textView2 = findViewById(R.id.food_ctext05);
        textView3 = findViewById(R.id.food_ctext08);
        textView4 = findViewById(R.id.food_ctext11);
        textView5 = findViewById(R.id.food_ctext14);
        textView6 = findViewById(R.id.food_ctext17);

        textView7 = findViewById(R.id.food_ctext04);
        textView8 = findViewById(R.id.food_ctext07);
        textView9 = findViewById(R.id.food_ctext10);
        textView10 = findViewById(R.id.food_ctext13);
        textView11 = findViewById(R.id.food_ctext16);
        textView12 = findViewById(R.id.food_ctext19);

        TabLayout tabLayout = findViewById(R.id.food_tab_layout);

        // Add Tab Selection Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getText().toString()) {
                    case "야채":
                        updateVegetableImages();
                        setupVegetableClickListeners();
                        updateVegetableInfo();
                        break;
                    case "과일":
                        updateFruitImages();
                        setupFruitClickListeners();
                        updateFruitInfo();
                        break;
                    case "육류":
                        updateMeatImages();
                        setupMeatClickListeners();
                        updateMeatInfo();
                        break;
                    case "기타":
                        updateElsesImages();
                        setupElseClickListeners();
                        updateElseInfo();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        setupFruitClickListeners(); // 초기화 시 기본 과일 이미지 및 클릭 리스너 설정
    }

    // 과일 이미지로 설정
    private void updateFruitImages() {
        ImageView1.setImageResource(R.drawable.peach);
        ImageView2.setImageResource(R.drawable.apple);
        ImageView3.setImageResource(R.drawable.watermelon);
        ImageView4.setImageResource(R.drawable.kiwi);
        ImageView5.setImageResource(R.drawable.shine_muscat);
        ImageView6.setImageResource(R.drawable.mango);
    }

    // 야채 이미지로 설정
    private void updateVegetableImages() {
        ImageView1.setImageResource(R.drawable.carrot);
        ImageView2.setImageResource(R.drawable.tomato);
        ImageView3.setImageResource(R.drawable.cucumber);
        ImageView4.setImageResource(R.drawable.paprica);
        ImageView5.setImageResource(R.drawable.brocoli);
        ImageView6.setImageResource(R.drawable.onion);
    }

    // 육류 이미지로 설정
    private void updateMeatImages() {
        ImageView1.setImageResource(R.drawable.meat_cow);
        ImageView2.setImageResource(R.drawable.pork);
        ImageView3.setImageResource(R.drawable.chicken);
        ImageView4.setImageResource(R.drawable.lamb);
        ImageView5.setImageResource(R.drawable.duck);
        ImageView6.setImageResource(R.drawable.turkey);
    }

    // 기타 이미지로 설정
    private void updateElsesImages() {
        ImageView1.setImageResource(R.drawable.mealkit);
        ImageView2.setImageResource(R.drawable.rice);
        ImageView3.setImageResource(R.drawable.oil);
        ImageView4.setImageResource(R.drawable.soy_sauce);
        ImageView5.setImageResource(R.drawable.noodle);
        ImageView6.setImageResource(R.drawable.sauce);
    }

    // 과일 클릭 리스너 설정
    private void setupFruitClickListeners() {
        ImageView1.setOnClickListener(v -> openMartSpecificActivity(R.drawable.peach, "납작 복숭아", "34,120원"));
        ImageView2.setOnClickListener(v -> openMartSpecificActivity(R.drawable.apple, "꿀 사과", "24,320원"));
        ImageView3.setOnClickListener(v -> openMartSpecificActivity(R.drawable.watermelon, "수박", "40,000원"));
        ImageView4.setOnClickListener(v -> openMartSpecificActivity(R.drawable.kiwi, "키위", "25,000원"));
        ImageView5.setOnClickListener(v -> openMartSpecificActivity(R.drawable.shine_muscat, "샤인머스캣", "40,000원"));
        ImageView6.setOnClickListener(v -> openMartSpecificActivity(R.drawable.mango, "망고", "24,320원"));
    }

    // 야채 클릭 리스너 설정
    private void setupVegetableClickListeners() {
        ImageView1.setOnClickListener(v -> openMartSpecificActivity(R.drawable.carrot, "당근", "2,500원"));
        ImageView2.setOnClickListener(v -> openMartSpecificActivity(R.drawable.tomato, "토마토", "3,000원"));
        ImageView3.setOnClickListener(v -> openMartSpecificActivity(R.drawable.cucumber, "오이", "1,500원"));
        ImageView4.setOnClickListener(v -> openMartSpecificActivity(R.drawable.paprica, "파프리카", "4,000원"));
        ImageView5.setOnClickListener(v -> openMartSpecificActivity(R.drawable.brocoli, "브로콜리", "3,500원"));
        ImageView6.setOnClickListener(v -> openMartSpecificActivity(R.drawable.onion, "양파", "2,800원"));
    }

    // 육류 클릭 리스너 설정
    private void setupMeatClickListeners() {
        ImageView1.setOnClickListener(v -> openMartSpecificActivity(R.drawable.meat_cow, "소고기", "25,000원"));
        ImageView2.setOnClickListener(v -> openMartSpecificActivity(R.drawable.meat_cow, "돼지고기", "15,000원"));
        ImageView3.setOnClickListener(v -> openMartSpecificActivity(R.drawable.meat_cow, "닭고기", "10,000원"));
        ImageView4.setOnClickListener(v -> openMartSpecificActivity(R.drawable.meat_cow, "양고기", "30,000원"));
        ImageView5.setOnClickListener(v -> openMartSpecificActivity(R.drawable.meat_cow, "오리고기", "20,000원"));
        ImageView6.setOnClickListener(v -> openMartSpecificActivity(R.drawable.meat_cow, "칠면조", "22,000원"));
    }

    // 기타 클릭 리스너 설정
    private void setupElseClickListeners() {
        ImageView1.setOnClickListener(v -> openMartSpecificActivity(R.drawable.mealkit, "밀키트", "12,000원"));
        ImageView2.setOnClickListener(v -> openMartSpecificActivity(R.drawable.mealkit, "쌀", "25,000원"));
        ImageView3.setOnClickListener(v -> openMartSpecificActivity(R.drawable.mealkit, "식용유", "10,000원"));
        ImageView4.setOnClickListener(v -> openMartSpecificActivity(R.drawable.mealkit, "간장", "5,000원"));
        ImageView5.setOnClickListener(v -> openMartSpecificActivity(R.drawable.mealkit, "면", "3,000원"));
        ImageView6.setOnClickListener(v -> openMartSpecificActivity(R.drawable.mealkit, "소스", "4,000원"));
    }

    private void updateFruitInfo() {
        textView1.setText("당도 선별 납작 복숭아");
        textView2.setText("꿀 부사 사과");
        textView3.setText("시원한 수박");
        textView4.setText("달달한 키위");
        textView5.setText("샤인머스캣");
        textView6.setText("망고");

        textView7.setText("34,120원");
        textView8.setText("24,320원");
        textView9.setText("40,000원");
        textView10.setText("25,000원");
        textView11.setText("40,000원");
        textView12.setText("24,320원");
    }

    private void updateVegetableInfo() {
        textView1.setText("당근");
        textView2.setText("토마토");
        textView3.setText("오이");
        textView4.setText("파프리카");
        textView5.setText("브로콜리");
        textView6.setText("양파");

        textView7.setText("2,500원");
        textView8.setText("3,000원");
        textView9.setText("1,500원");
        textView10.setText("4,000원");
        textView11.setText("3,500원");
        textView12.setText("2,800원");
    }

    private void updateMeatInfo() {
        textView1.setText("소고기");
        textView2.setText("돼지고기");
        textView3.setText("닭고기");
        textView4.setText("양고기");
        textView5.setText("오리고기");
        textView6.setText("칠면조");

        textView7.setText("25,000원");
        textView8.setText("15,000원");
        textView9.setText("10,000원");
        textView10.setText("30,000원");
        textView11.setText("20,000원");
        textView12.setText("22,000원");
    }

    private void updateElseInfo() {
        textView1.setText("밀키트");
        textView2.setText("쌀");
        textView3.setText("식용유");
        textView4.setText("간장");
        textView5.setText("면");
        textView6.setText("소스");

        textView7.setText("12,000원");
        textView8.setText("25,000원");
        textView9.setText("10,000원");
        textView10.setText("5,000원");
        textView11.setText("3,000원");
        textView12.setText("4,000원");
    }

    // 상품 세부 정보로 이동
    private void openMartSpecificActivity(int imageResource, String itemName, String itemPrice) {
        Intent intent = new Intent(FoodCategoryActivity.this, MartSpecific.class);
        intent.putExtra("image_resource", imageResource);
        intent.putExtra("item_name", itemName);
        intent.putExtra("item_price", itemPrice);
        startActivity(intent);
    }
}