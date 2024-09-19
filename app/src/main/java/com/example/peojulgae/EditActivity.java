package com.example.peojulgae;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class EditActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // 이미지 선택 요청 코드

    private EditText editFoodName, editFoodDescription, editFoodPrice, editFoodDiscount, editFoodQuantity;
    private ImageView editFoodImageUrl;
    private Button saveButton;
    private DatabaseReference dbRef;
    private StorageReference storageRef;
    private String foodId;
    private Food food;
    private Uri imageUri; // 선택한 이미지의 URI 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editFoodName = findViewById(R.id.edit_food_name);
        editFoodDescription = findViewById(R.id.edit_food_description);
        editFoodPrice = findViewById(R.id.edit_food_price);
        editFoodDiscount = findViewById(R.id.edit_food_discount);
        editFoodQuantity = findViewById(R.id.edit_food_quantity);
        editFoodImageUrl = findViewById(R.id.edit_food_image);
        saveButton = findViewById(R.id.btn_save);

        foodId = getIntent().getStringExtra("foodId");
        dbRef = FirebaseDatabase.getInstance().getReference("Foods").child(foodId);
        storageRef = FirebaseStorage.getInstance().getReference("FoodImages");

        loadFoodDetails(); // 데이터 로드

        saveButton.setOnClickListener(v -> saveFoodDetails());

        // 이미지 클릭 리스너 추가
        editFoodImageUrl.setOnClickListener(v -> openImageChooser());
    }

    private void loadFoodDetails() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                food = snapshot.getValue(Food.class);
                if (food != null) {
                    editFoodName.setText(food.getName());
                    editFoodDescription.setText(food.getDescription());
                    editFoodPrice.setText(food.getPrice());
                    editFoodDiscount.setText(String.valueOf(food.getDiscount())); // 숫자를 문자열로 변환
                    editFoodQuantity.setText(String.valueOf(food.getQuantity()));

                    // Firebase에서 가져온 메인 이미지 URL을 ImageView에 로드
                    Glide.with(EditActivity.this).load(food.getMainImageUrl()).into(editFoodImageUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditActivity.this, "데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "이미지 선택"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                // 선택한 이미지를 ImageView에 표시
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                editFoodImageUrl.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFoodDetails() {
        String name = editFoodName.getText().toString().trim();
        String description = editFoodDescription.getText().toString().trim();
        String price = editFoodPrice.getText().toString().trim();
        String discountString = editFoodDiscount.getText().toString().trim();
        String quantityString = editFoodQuantity.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price) ||
                TextUtils.isEmpty(discountString) || TextUtils.isEmpty(quantityString)) {
            Toast.makeText(this, "모든 필드를 채워주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int discount;
        int quantity;
        try {
            discount = Integer.parseInt(discountString);
            quantity = Integer.parseInt(quantityString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "할인율과 수량은 숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri != null) {
            // 이미지 업로드 및 정보 저장
            uploadImageAndSaveFoodDetails(name, description, price, discount, quantity);
        } else {
            // 이미지를 변경하지 않았을 경우 기존 정보 업데이트
            saveFoodToDatabase(name, description, price, discount, quantity, food.getMainImageUrl());
        }
    }

    private void uploadImageAndSaveFoodDetails(String name, String description, String price, int discount, int quantity) {
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpg");

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            saveFoodToDatabase(name, description, price, discount, quantity, imageUrl);
        })).addOnFailureListener(e -> {
            Toast.makeText(EditActivity.this, "이미지 업로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void saveFoodToDatabase(String name, String description, String price, int discount, int quantity, String imageUrl) {
        Food updatedFood = new Food(foodId, name, description, price, discount, quantity, food.getImageUrl(), imageUrl, food.getCategories(), food.getUserId());

        dbRef.setValue(updatedFood).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "음식 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                finish(); // 액티비티 종료하여 이전 화면으로 돌아감
            } else {
                Toast.makeText(this, "음식 정보 수정 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
