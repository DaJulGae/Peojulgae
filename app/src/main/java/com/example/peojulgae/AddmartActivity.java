package com.example.peojulgae;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddmartActivity extends AppCompatActivity {

    private EditText martNameEditText;
    private EditText martAddressEditText;
    private EditText martPhoneEditText;
    private Button selectStartTimeButton;
    private Button selectEndTimeButton;
    private TextView startTimeText;
    private TextView endTimeText;
    private Button addMartButton;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private String startTime;
    private String endTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 새 계정 테스트
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmart);

        martNameEditText = findViewById(R.id.mart_name);
        martAddressEditText = findViewById(R.id.mart_address);
        martPhoneEditText = findViewById(R.id.mart_phone);
        selectStartTimeButton = findViewById(R.id.select_start_time_button);
        selectEndTimeButton = findViewById(R.id.select_end_time_button);
        startTimeText = findViewById(R.id.start_time_text);
        endTimeText = findViewById(R.id.end_time_text);
        addMartButton = findViewById(R.id.add_mart_button);

        // Firebase Database 및 Auth 초기화
        mDatabase = FirebaseDatabase.getInstance().getReference("marts");
        mAuth = FirebaseAuth.getInstance();

        selectStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(true);
            }
        });

        selectEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(false);
            }
        });

        addMartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMart();
            }
        });
    }

    private void showTimePickerDialog(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                if (isStartTime) {
                    startTime = time;
                    startTimeText.setText("시작 시간: " + time);
                } else {
                    endTime = time;
                    endTimeText.setText("종료 시간: " + time);
                }
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void addMart() {
        String name = martNameEditText.getText().toString().trim();
        String address = martAddressEditText.getText().toString().trim();
        String phone = martPhoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // 고유 ID 생성
            String martId = mDatabase.push().getKey();

            // Mart 객체 생성
            Mart mart = new Mart(name, address, phone, startTime + " - " + endTime, userId);

            // Firebase에 저장
            if (martId != null) {
                mDatabase.child(martId).setValue(mart).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddmartActivity.this, "마트가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        // 마트 정보를 상품 등록 액티비티로 전달
                        Intent intent = new Intent(AddmartActivity.this, AddproductActivity.class);
                        intent.putExtra("martId", martId);
                        intent.putExtra("martName", name);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddmartActivity.this, "등록 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // Mart 모델 클래스
    public static class Mart {
        public String name;
        public String address;
        public String phone;
        public String hours;
        public String userId;

        public Mart() {
            // 기본 생성자 필요
        }

        public Mart(String name, String address, String phone, String hours, String userId) {
            this.name = name;
            this.address = address;
            this.phone = phone;
            this.hours = hours;
            this.userId = userId;
        }
    }
}
