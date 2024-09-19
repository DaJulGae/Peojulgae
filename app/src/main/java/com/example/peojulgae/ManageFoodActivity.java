package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ManageFoodActivity extends AppCompatActivity implements FoodManageAdapter.OnItemClickListener {

    private RecyclerView manageRecyclerView;
    private FoodManageAdapter adapter;
    private List<Food> foodList;
    private DatabaseReference dbRef;
    private String currentUserId;
    private String restaurantId; // 음식점 ID를 저장할 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_food);

        manageRecyclerView = findViewById(R.id.manageRecyclerView);
        manageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        manageRecyclerView.setHasFixedSize(true);

        foodList = new ArrayList<>();
        adapter = new FoodManageAdapter(foodList, this, this);
        manageRecyclerView.setAdapter(adapter);

        // 현재 사용자 ID 가져오기
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            // 사용자 restaurantId 가져오기
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
            userRef.child("restaurantId").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        restaurantId = snapshot.getValue(String.class);
                        if (restaurantId != null) {
                            // dbRef를 restaurantId에 따라 설정
                            dbRef = FirebaseDatabase.getInstance().getReference()
                                    .child("Restaurants").child(restaurantId).child("Foods");
                            loadUserFoods(); // 음식점의 음식 데이터 로드
                        } else {
                            Toast.makeText(ManageFoodActivity.this, "음식점 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ManageFoodActivity.this, "음식점이 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ManageFoodActivity.this, "데이터베이스 오류: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserFoods() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    Food food = foodSnapshot.getValue(Food.class);
                    if (food != null) {
                        foodList.add(food);
                    }
                }
                // 데이터 변경 후 어댑터에 알림
                adapter.notifyDataSetChanged();

                if (foodList.isEmpty()) {
                    Toast.makeText(ManageFoodActivity.this, "등록된 음식이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageFoodActivity.this, "데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditClick(Food food) {
        openEditActivity(food.getFoodId());
    }

    @Override
    public void onDeleteClick(Food food) {
        confirmDeleteFood(food.getFoodId());
    }

    private void openEditActivity(String foodId) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("foodId", foodId);
        startActivity(intent);
    }

    private void confirmDeleteFood(String foodId) {
        new AlertDialog.Builder(this)
                .setTitle("삭제 확인")
                .setMessage("정말 이 항목을 삭제하시겠습니까?")
                .setPositiveButton("예", (dialog, which) -> deleteFood(foodId))
                .setNegativeButton("아니오", null)
                .show();
    }

    private void deleteFood(String foodId) {
        // 삭제할 음식 경로를 restaurantId 하위의 Foods 경로로 설정
        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference()
                .child("Restaurants").child(restaurantId).child("Foods").child(foodId);
        foodRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "음식이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                loadUserFoods(); // 삭제 후 음식 목록 새로고침
            } else {
                Toast.makeText(this, "삭제 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
