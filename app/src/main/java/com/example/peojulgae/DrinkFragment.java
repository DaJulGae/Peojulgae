package com.example.peojulgae;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DrinkFragment extends Fragment {

    private RecyclerView drinkRecyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private List<Food> filteredFoodList;
    private DatabaseReference dbRef;
    private String selectedCategory = "음료"; // 기본 카테고리
    private TextView categoryTitle; // TextView 참조 변수
    private String restaurantId; // 사용자 음식점 ID를 저장할 변수
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_drink_fragment, container, false);

        // FirebaseAuth 초기화
        auth = FirebaseAuth.getInstance();

        // UI 요소 초기화
        categoryTitle = view.findViewById(R.id.categoryTitle);
        drinkRecyclerView = view.findViewById(R.id.drinkRecyclerView);
        drinkRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        foodList = new ArrayList<>();
        filteredFoodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(filteredFoodList, getContext());

        // 어댑터를 RecyclerView에 설정
        drinkRecyclerView.setAdapter(foodAdapter);

        // 사용자 ID 가져오기
        String userId = auth.getCurrentUser().getUid();

        // 사용자의 restaurantId를 가져오기 위해 데이터베이스 참조
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.child("restaurantId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    restaurantId = snapshot.getValue(String.class);
                    if (restaurantId != null) {
                        // restaurantId를 통해 dbRef 설정
                        dbRef = FirebaseDatabase.getInstance().getReference()
                                .child("Restaurants").child(restaurantId).child("Foods");

                        // 기본 카테고리 데이터 로드
                        loadFoods(selectedCategory);
                    } else {
                        Toast.makeText(getContext(), "음식점 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "음식점이 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "데이터베이스 오류: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // 카테고리 버튼 클릭 이벤트 추가
        setCategoryButtons(view);

        return view;
    }

    // 카테고리별 데이터 로드
    private void loadFoods(String category) {
        if (dbRef == null) {
            // dbRef가 null인 경우 (restaurantId가 아직 null이므로)
            Toast.makeText(getContext(), "음식점 정보를 불러오는 중입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();

                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    Food food = foodSnapshot.getValue(Food.class);
                    if (food != null && food.getCategories() != null && food.getCategories().contains(category)) {
                        foodList.add(food);
                    }
                }
                filteredFoodList.clear();
                filteredFoodList.addAll(foodList);
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 카테고리 버튼 설정
    private void setCategoryButtons(View view) {
        Button pizzaButton = view.findViewById(R.id.category_pizza);
        Button chickenButton = view.findViewById(R.id.category_chicken);
        Button koreanButton = view.findViewById(R.id.category_korean);
        Button japaneseButton = view.findViewById(R.id.category_japanese);
        Button chineseButton = view.findViewById(R.id.category_chinese);
        Button snackButton = view.findViewById(R.id.category_snack);
        Button dessertButton = view.findViewById(R.id.category_dessert);
        Button drinkButton = view.findViewById(R.id.category_drink);

        // 각 카테고리 버튼 클릭 시 해당 카테고리 데이터 로드 및 라벨 업데이트
        pizzaButton.setOnClickListener(v -> {
            selectedCategory = "피자";
            updateCategoryTitle(selectedCategory); // TextView 업데이트
            loadFoods(selectedCategory);
        });
        chickenButton.setOnClickListener(v -> {
            selectedCategory = "치킨";
            updateCategoryTitle(selectedCategory); // TextView 업데이트
            loadFoods(selectedCategory);
        });
        koreanButton.setOnClickListener(v -> {
            selectedCategory = "한식";
            updateCategoryTitle(selectedCategory); // TextView 업데이트
            loadFoods(selectedCategory);
        });
        japaneseButton.setOnClickListener(v -> {
            selectedCategory = "일식";
            updateCategoryTitle(selectedCategory); // TextView 업데이트
            loadFoods(selectedCategory);
        });
        chineseButton.setOnClickListener(v -> {
            selectedCategory = "중식";
            updateCategoryTitle(selectedCategory); // TextView 업데이트
            loadFoods(selectedCategory);
        });
        snackButton.setOnClickListener(v -> {
            selectedCategory = "분식";
            updateCategoryTitle(selectedCategory); // TextView 업데이트
            loadFoods(selectedCategory);
        });
        dessertButton.setOnClickListener(v -> {
            selectedCategory = "디저트";
            updateCategoryTitle(selectedCategory); // TextView 업데이트
            loadFoods(selectedCategory);
        });
        drinkButton.setOnClickListener(v -> {
            selectedCategory = "음료";
            updateCategoryTitle(selectedCategory); // TextView 업데이트
            loadFoods(selectedCategory);
        });
    }

    // 카테고리 라벨 업데이트 메서드
    private void updateCategoryTitle(String category) {
        categoryTitle.setText(category); // 선택된 카테고리로 라벨 변경
    }
}
