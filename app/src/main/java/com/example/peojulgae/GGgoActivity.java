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
        setContentView(R.layout.gggo_display); // 주어진 XML 레이아웃 파일을 설정

        // ImageView에 대한 참조를 연결합니다.
        ImageView nicericeImageView = findViewById(R.id.nicerice);
        ImageView noodledoodleImageView = findViewById(R.id.noodledoodle);
        ImageView meatmayoImageView = findViewById(R.id.meatmayo);
        ImageView spicyporkImageView = findViewById(R.id.spicypork);

        // 나이스 라이스 클릭 시 FoodListActivity로 이동
        nicericeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GGgoActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.nicerice); // 이미지 리소스 전달
                intent.putExtra("food_name", "나이스 라이스"); // 음식 이름 전달
                startActivity(intent);
            }
        });

        // 누들두들 클릭 시 FoodListActivity로 이동
        noodledoodleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GGgoActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.noodledoodle); // 이미지 리소스 전달
                intent.putExtra("food_name", "누들두들"); // 음식 이름 전달
                startActivity(intent);
            }
        });

        // 고기마요 클릭 시 FoodListActivity로 이동
        meatmayoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GGgoActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.meatmayo); // 이미지 리소스 전달
                intent.putExtra("food_name", "고기마요"); // 음식 이름 전달
                startActivity(intent);
            }
        });

        // 제육볶음마요 클릭 시 FoodListActivity로 이동
        spicyporkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GGgoActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.spicypork); // 이미지 리소스 전달
                intent.putExtra("food_name", "제육볶음마요"); // 음식 이름 전달
                startActivity(intent);
            }
        });
    }
}