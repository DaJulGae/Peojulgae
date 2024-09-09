package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
//test
public class BaloonActivity extends AppCompatActivity { // 가나 점보 돈까스
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_display);

        ImageView normalDonggasImageView = findViewById(R.id.normaldonggas);
        ImageView cheeseDonggasImageView = findViewById(R.id.cheesedonggas);
        ImageView eggDonggasImageView = findViewById(R.id.eggdongas);
        ImageView eggCheeseDonggasImageView = findViewById(R.id.eggcheese);
        ImageView MulnaengmyeonImageView = findViewById(R.id.Mulnaengmyeon);

        normalDonggasImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaloonActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.store_gana);
                intent.putExtra("food_name", "가나점보 돈까스");
                startActivity(intent);
            }
        });

        cheeseDonggasImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaloonActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.cheese);
                intent.putExtra("food_name", "고구마 치즈 돈까스");
                startActivity(intent);
            }
        });
        eggDonggasImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaloonActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.egg_ganajumbo);
                intent.putExtra("food_name", "egg + 가나점보 돈까스");
                startActivity(intent);
            }
        });

        eggCheeseDonggasImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaloonActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.cheese2);
                intent.putExtra("food_name", "egg + 치즈 돈까스");
                startActivity(intent);
            }
        });
        MulnaengmyeonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaloonActivity.this, FoodListActivity.class);
                intent.putExtra("image_resource", R.drawable.weter);
                intent.putExtra("food_name", "물냉면");
                startActivity(intent);
            }
        });
    }
}
