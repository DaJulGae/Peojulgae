package com.example.peojulgae;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddproductActivity extends AppCompatActivity {

    private static final int SELECT_PHOTOS_REQUEST_CODE = 1;
    private static final int SELECT_MAIN_IMAGE_REQUEST_CODE = 2;
    private static final String TAG = "AddproductActivity";

    private EditText foodName, foodDescription, foodPrice;
    private TextView selectedPhotosText, discountText, quantityText;
    private Button selectPhotosButton, registerFoodButton, selectMainImageButton;
    private SeekBar discountSeekBar, quantitySeekBar;
    private CheckBox categoryPizza, categoryChicken, categoryDessert, categoryDrink, categorySnack, categoryJapanese, categoryChinese, categoryKorean;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private DatabaseReference db;
    private List<Uri> photoUris;
    private Uri mainImageUri;
    private int discount = 0;
    private int quantity = 1;
    private String restaurantId; // 사용자의 음식점 ID 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);

        // UI 요소 초기화
        foodName = findViewById(R.id.foodName);
        foodDescription = findViewById(R.id.foodDescription);
        foodPrice = findViewById(R.id.foodPrice);
        selectedPhotosText = findViewById(R.id.selectedFoodPhotosText);
        discountText = findViewById(R.id.discountText);
        quantityText = findViewById(R.id.quantityText);
        selectPhotosButton = findViewById(R.id.selectFoodPhotosButton);
        registerFoodButton = findViewById(R.id.registerFoodButton);
        selectMainImageButton = findViewById(R.id.selectMainImageButton);
        discountSeekBar = findViewById(R.id.discountSeekBar);
        quantitySeekBar = findViewById(R.id.quantitySeekBar);

        // 카테고리 체크박스 초기화
        categoryPizza = findViewById(R.id.categoryPizza);
        categoryChicken = findViewById(R.id.categoryChicken);
        categoryDessert = findViewById(R.id.categoryDessert);
        categoryDrink = findViewById(R.id.categoryDrink);
        categorySnack = findViewById(R.id.categorySnack);
        categoryJapanese = findViewById(R.id.categoryJapanese);
        categoryChinese = findViewById(R.id.categoryChinese);
        categoryKorean = findViewById(R.id.categoryKorean);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db = FirebaseDatabase.getInstance().getReference(); // 경로 변경: 'FoodS' 대신 루트 경로로 수정
        photoUris = new ArrayList<>();

        // 사용자 restaurantId 확인
        String userId = auth.getCurrentUser().getUid();
        db.child("Users").child(userId).child("restaurantId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    restaurantId = snapshot.getValue(String.class);
                } else {
                    Toast.makeText(AddproductActivity.this, "음식점이 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // 음식점이 없으면 액티비티 종료
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddproductActivity.this, "데이터베이스 오류: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 이벤트 리스너 설정
        selectPhotosButton.setOnClickListener(v -> selectPhotos());
        selectMainImageButton.setOnClickListener(v -> selectMainImage());
        registerFoodButton.setOnClickListener(v -> registerFood());

        discountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                discount = progress;
                discountText.setText("할인율: " + discount + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        quantitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quantity = progress + 1;
                quantityText.setText("수량: " + quantity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // 다중 사진 선택 메소드
    private void selectPhotos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "사진 선택"), SELECT_PHOTOS_REQUEST_CODE);
    }

    // 메인 이미지 선택 메소드
    private void selectMainImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "메인 이미지 선택"), SELECT_MAIN_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
            // 여러 장의 사진 선택
            if (requestCode == SELECT_PHOTOS_REQUEST_CODE) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        photoUris.add(imageUri);
                    }
                } else if (data.getData() != null) {
                    photoUris.add(data.getData());
                }
                selectedPhotosText.setText(photoUris.size() + "장의 사진 선택됨");
            }
            // 메인 이미지 선택
            else if (requestCode == SELECT_MAIN_IMAGE_REQUEST_CODE) {
                mainImageUri = data.getData();
                selectMainImageButton.setText("메인 이미지 선택됨");
            }
        }
    }

    // 음식 정보 등록 메소드
    private void registerFood() {
        String name = foodName.getText().toString().trim();
        String description = foodDescription.getText().toString().trim();
        String price = foodPrice.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price)) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (restaurantId == null) {
            Toast.makeText(this, "음식점이 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String foodId = db.child("Restaurants").child(restaurantId).child("Foods").push().getKey(); // 음식 ID 생성
        if (foodId == null) {
            Toast.makeText(this, "foodId 생성 실패", Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference foodRef = db.child("Restaurants").child(restaurantId).child("Foods").child(foodId);

        final Map<String, Object> foodData = new HashMap<>();
        foodData.put("foodId", foodId); // foodId 추가
        foodData.put("name", name);
        foodData.put("description", description);
        foodData.put("price", price);
        foodData.put("discount", discount);
        foodData.put("quantity", quantity);
        foodData.put("restaurantId", restaurantId); // restaurantId 추가
        foodData.put("userId", auth.getCurrentUser().getUid());

        // 카테고리 선택 처리
        List<String> categories = new ArrayList<>();
        if (categoryPizza.isChecked()) categories.add("피자");
        if (categoryChicken.isChecked()) categories.add("치킨");
        if (categoryDessert.isChecked()) categories.add("디저트");
        if (categoryDrink.isChecked()) categories.add("음료");
        if (categorySnack.isChecked()) categories.add("분식");
        if (categoryJapanese.isChecked()) categories.add("일식");
        if (categoryChinese.isChecked()) categories.add("중식");
        if (categoryKorean.isChecked()) categories.add("한식");
        foodData.put("categories", categories);

        uploadPhotosAndSave(foodRef, foodData);
    }

    // 사진 업로드 및 저장 메소드
    private void uploadPhotosAndSave(DatabaseReference foodRef, Map<String, Object> foodData) {
        StorageReference storageRef = storage.getReference().child("food_photos").child(foodRef.getKey());

        // 메인 이미지 업로드
        if (mainImageUri != null) {
            String mainImageFileName = UUID.randomUUID().toString() + ".jpg"; // 고유한 파일 이름 생성
            uploadImage(storageRef.child(mainImageFileName), mainImageUri, url -> {
                foodData.put("mainImageUrl", url);
                saveToDatabase(foodRef, foodData);
            });
        }

        // 다중 사진 업로드
        if (!photoUris.isEmpty()) {
            final List<String> photoUrls = new ArrayList<>();
            for (Uri uri : photoUris) {
                String photoFileName = UUID.randomUUID().toString() + ".jpg"; // 고유한 파일 이름 생성
                uploadImage(storageRef.child(photoFileName), uri, photoUrls::add);
            }
            foodData.put("photoUrls", photoUrls);
        }

        // 데이터 저장
        saveToDatabase(foodRef, foodData);
    }

    // 이미지 업로드 메소드
    private void uploadImage(StorageReference ref, Uri uri, OnCompleteListener<String> onComplete) {
        ref.putFile(uri).addOnSuccessListener(taskSnapshot ->
                ref.getDownloadUrl().addOnSuccessListener(url -> onComplete.onComplete(url.toString()))
        ).addOnFailureListener(e -> {
            Log.e(TAG, "파일 업로드 실패: " + e.getMessage());
            Toast.makeText(this, "파일 업로드 실패", Toast.LENGTH_SHORT).show();
        });
    }

    // Firebase에 음식 데이터 저장 메소드
    private void saveToDatabase(DatabaseReference foodRef, Map<String, Object> foodData) {
        foodRef.setValue(foodData).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "음식 등록 완료", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Log.e(TAG, "데이터베이스 저장 실패: " + e.getMessage());
            Toast.makeText(this, "데이터베이스 저장 실패", Toast.LENGTH_SHORT).show();
        });
    }

    // 콜백 인터페이스
    private interface OnCompleteListener<T> {
        void onComplete(T result);
    }
}

