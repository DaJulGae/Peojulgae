package com.example.peojulgae;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class AddmartproductActivity extends AppCompatActivity {

    private static final int SELECT_PHOTOS_REQUEST_CODE = 1;
    private static final String TAG = "AddmartproductActivity";

    private EditText productName, productDescription, productPrice;
    private TextView selectedPhotosText, discountText;
    private Button selectPhotosButton, registerProductButton;
    private SeekBar discountSeekBar;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private DatabaseReference db;
    private List<Uri> photoUris;
    private int discount = 0;
    private String martId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmartproduct);

        // UI 요소 초기화
        productName = findViewById(R.id.edit_product_name);
        productDescription = findViewById(R.id.edit_product_description);
        productPrice = findViewById(R.id.edit_product_price);
        selectedPhotosText = findViewById(R.id.selected_photos_text);
        discountText = findViewById(R.id.tv_discount_label);
        selectPhotosButton = findViewById(R.id.btn_select_images);
        registerProductButton = findViewById(R.id.btn_register_product);
        discountSeekBar = findViewById(R.id.seekbar_discount);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        photoUris = new ArrayList<>();

        // 현재 사용자 ID로부터 martId 가져오기
        String userId = auth.getCurrentUser().getUid();
        db.child("marts").child("Users").child(userId).child("martId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    martId = snapshot.getValue(String.class);
                    Log.d(TAG, "가져온 마트 ID: " + martId); // martId 확인 로그 추가
                } else {
                    Log.e(TAG, "마트가 등록되지 않음. 마트 ID 없음.");
                    Toast.makeText(AddmartproductActivity.this, "마트가 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // 마트가 없으면 액티비티 종료
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "데이터베이스 오류: " + error.getMessage());
                Toast.makeText(AddmartproductActivity.this, "데이터베이스 오류: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 이벤트 리스너 설정
        selectPhotosButton.setOnClickListener(v -> selectPhotos());
        registerProductButton.setOnClickListener(v -> registerProduct());

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
    }

    // 다중 사진 선택 메소드
    private void selectPhotos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "사진 선택"), SELECT_PHOTOS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && data != null) {
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
                selectedPhotosText.setVisibility(View.VISIBLE);
            }
        }
    }

    // 상품 정보 등록 메소드
    private void registerProduct() {
        String name = productName.getText().toString().trim();
        String description = productDescription.getText().toString().trim();
        String price = productPrice.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(price)) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (martId == null) {
            Toast.makeText(this, "마트가 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String productId = db.child("Marts").child(martId).child("Products").push().getKey(); // 상품 ID 생성
        if (productId == null) {
            Toast.makeText(this, "상품 ID 생성 실패", Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference productRef = db.child("Marts").child(martId).child("Products").child(productId);

        final Map<String, Object> productData = new HashMap<>();
        productData.put("productId", productId); // 상품 ID 추가
        productData.put("name", name);
        productData.put("description", description);
        productData.put("price", price);
        productData.put("discount", discount);
        productData.put("martId", martId); // 마트 ID 추가
        productData.put("userId", auth.getCurrentUser().getUid());

        uploadPhotosAndSave(productRef, productData);
    }

    // 사진 업로드 및 저장 메소드
    private void uploadPhotosAndSave(DatabaseReference productRef, Map<String, Object> productData) {
        StorageReference storageRef = storage.getReference().child("product_photos").child(productRef.getKey());

        // 다중 사진 업로드
        if (!photoUris.isEmpty()) {
            final List<String> photoUrls = new ArrayList<>();
            for (Uri uri : photoUris) {
                String photoFileName = UUID.randomUUID().toString() + ".jpg"; // 고유한 파일 이름 생성
                UploadTask uploadTask = storageRef.child(photoFileName).putFile(uri);

                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    // 업로드 성공 시 다운로드 URL 가져오기
                    Task<Uri> downloadUriTask = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUriTask.addOnSuccessListener(uri1 -> {
                        String downloadUrl = uri1.toString();
                        photoUrls.add(downloadUrl);

                        // 모든 사진이 업로드되면 데이터베이스에 저장
                        if (photoUrls.size() == photoUris.size()) {
                            productData.put("photoUrls", photoUrls);
                            saveToDatabase(productRef, productData);
                        }
                    });
                }).addOnFailureListener(e -> {
                    Log.e(TAG, "파일 업로드 실패: " + e.getMessage());
                    Toast.makeText(this, "파일 업로드 실패", Toast.LENGTH_SHORT).show();
                });
            }
        } else {
            // 사진이 없을 경우 바로 데이터 저장
            saveToDatabase(productRef, productData);
        }
    }

    // Firebase에 상품 데이터 저장 메소드
    private void saveToDatabase(DatabaseReference productRef, Map<String, Object> productData) {
        productRef.setValue(productData).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "상품 등록 완료", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Log.e(TAG, "데이터베이스 저장 실패: " + e.getMessage());
            Toast.makeText(this, "데이터베이스 저장 실패", Toast.LENGTH_SHORT).show();
        });
    }
}
