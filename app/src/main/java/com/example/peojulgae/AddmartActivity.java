package com.example.peojulgae;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddmartActivity extends AppCompatActivity {

    private static final int SELECT_PHOTOS_REQUEST_CODE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    private EditText martNameEditText, martAddressEditText, martPhoneEditText;
    private TextView selectedPhotosText, operatingHoursText;
    private Button selectPhotosButton;
    private Button addMartButton;
    private Button selectOperatingHoursButton;
    private ProgressBar progressBar;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private List<Uri> photoUris;
    private String startTime, endTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmart);

        // UI 요소 초기화
        martNameEditText = findViewById(R.id.mart_name);
        martAddressEditText = findViewById(R.id.mart_address);
        martPhoneEditText = findViewById(R.id.mart_phone);
        selectedPhotosText = findViewById(R.id.selectedPhotosText);
        operatingHoursText = findViewById(R.id.operatingHoursText);
        selectPhotosButton = findViewById(R.id.selectPhotosButton);
        addMartButton = findViewById(R.id.add_mart_button);
        selectOperatingHoursButton = findViewById(R.id.OpeningHoursButton);

        // Firebase Database 및 Auth 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference("marts");
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        photoUris = new ArrayList<>();

        // 기존 마트 존재 여부 확인
        checkExistingMart();

        // 사진 선택 Button 클릭 리스너
        selectPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestStoragePermission();
            }
        });

        // 운영 시간 선택 Button 클릭 리스너
        selectOperatingHoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOperatingHours();
            }
        });

        // 마트 등록 Button 클릭 리스너
        addMartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMart();
            }
        });
    }

    // 기존 마트 존재 여부 확인
    private void checkExistingMart() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(userId).child("martId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(AddmartActivity.this, "이미 등록된 마트가 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddmartActivity.this, "데이터베이스 오류: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 저장소 퍼미션 확인 및 요청
    private void checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        } else {
            selectPhotos();
        }
    }

    // 퍼미션 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPhotos();
            } else {
                Toast.makeText(this, "저장소 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 이미지 선택 메서드
    private void selectPhotos() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "사진 선택"), SELECT_PHOTOS_REQUEST_CODE);
    }

    // 이미지 선택 후 결과 처리 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTOS_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    photoUris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                photoUris.add(data.getData());
            }
            selectedPhotosText.setText(photoUris.size() + "장의 사진 선택됨");
        }
    }

    // 운영 시간 선택 메서드
    private void selectOperatingHours() {
        // 시작 시간 선택
        TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startTime = String.format("%02d:%02d", hourOfDay, minute);
                // 종료 시간 선택
                selectEndTime();
            }
        };

        TimePickerDialog startTimePickerDialog = new TimePickerDialog(this, startTimeSetListener, 9, 0, true);
        startTimePickerDialog.setTitle("운영 시작 시간 선택");
        startTimePickerDialog.show();
    }

    // 종료 시간 선택 메서드
    private void selectEndTime() {
        TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endTime = String.format("%02d:%02d", hourOfDay, minute);
                operatingHoursText.setText("운영시간: " + startTime + " - " + endTime);
            }
        };

        TimePickerDialog endTimePickerDialog = new TimePickerDialog(this, endTimeSetListener, 21, 0, true);
        endTimePickerDialog.setTitle("운영 종료 시간 선택");
        endTimePickerDialog.show();
    }

    // 마트 등록 메서드
    private void addMart() {
        String name = martNameEditText.getText().toString().trim();
        String address = martAddressEditText.getText().toString().trim();
        String phone = martPhoneEditText.getText().toString().trim();

        // 필드가 비어 있는지 확인
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 종료 시간이 시작 시간보다 빠를 경우 예외 처리
        if (!isValidTimeRange(startTime, endTime)) {
            Toast.makeText(this, "올바른 영업 시간을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String martId = mDatabase.push().getKey();

            Map<String, Object> mart = new HashMap<>();
            mart.put("name", name);
            mart.put("address", address);
            mart.put("phone", phone);
            mart.put("hours", startTime + " - " + endTime);
            mart.put("userId", userId);


            if (photoUris.isEmpty()) {
                // 사진이 없는 경우 바로 데이터베이스에 저장
                mDatabase.child("Marts").child(martId).setValue(mart)
                        .addOnSuccessListener(aVoid -> {
                            mDatabase.child("Users").child(userId).child("martId").setValue(martId);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddmartActivity.this, "마트 등록 완료", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            progressBar.setVisibility(View.GONE);
                            addMartButton.setEnabled(true);
                            Toast.makeText(AddmartActivity.this, "오류: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // 사진이 있는 경우 업로드 후 데이터베이스에 저장
                uploadPhotosAndSaveMart(martId, mart);
            }
        } else {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 사진 업로드 및 마트 데이터베이스 저장 메서드
    private void uploadPhotosAndSaveMart(final String martId, final Map<String, Object> mart) {
        final List<String> photoUrls = new ArrayList<>();
        StorageReference storageRef = storage.getReference().child("mart_photos").child(martId);

        for (Uri uri : photoUris) {
            final StorageReference photoRef = storageRef.child(uri.getLastPathSegment());
            photoRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        photoRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    photoUrls.add(task.getResult().toString());
                                    if (photoUrls.size() == photoUris.size()) {
                                        mart.put("photoUrls", photoUrls);
                                        mDatabase.child("Marts").child(martId).setValue(mart)
                                                .addOnSuccessListener(aVoid -> {
                                                    mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("martId").setValue(martId);
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(AddmartActivity.this, "마트 등록 완료", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> {
                                                    progressBar.setVisibility(View.GONE);
                                                    addMartButton.setEnabled(true);
                                                    Toast.makeText(AddmartActivity.this, "오류: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    addMartButton.setEnabled(true);
                                    Toast.makeText(AddmartActivity.this, "사진 URL 가져오기 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        addMartButton.setEnabled(true);
                        Toast.makeText(AddmartActivity.this, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // 영업 시간 유효성 검사 메서드
    private boolean isValidTimeRange(String startTime, String endTime) {
        try {
            String[] start = startTime.split(":");
            String[] end = endTime.split(":");
            int startHour = Integer.parseInt(start[0]);
            int startMinute = Integer.parseInt(start[1]);
            int endHour = Integer.parseInt(end[0]);
            int endMinute = Integer.parseInt(end[1]);

            // 종료 시간이 시작 시간보다 빠르거나 같으면 false 반환
            return endHour > startHour || (endHour == startHour && endMinute > startMinute);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
