package com.example.peojulgae;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class CategoryItemsFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";

    private EditText searchEditText;
    private RecyclerView categoryItemsRecyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private List<Food> filteredFoodList;
    private DatabaseReference dbRef;
    private String currentCategory;
    private String restaurantId; // 음식점 ID를 저장할 변수
    private FirebaseAuth auth;

    public CategoryItemsFragment() {
        // 기본 생성자 필요
    }

    public static CategoryItemsFragment newInstance(String category) {
        CategoryItemsFragment fragment = new CategoryItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentCategory = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_items, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        categoryItemsRecyclerView = view.findViewById(R.id.categoryItemsRecyclerView);
        categoryItemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        foodList = new ArrayList<>();
        filteredFoodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(filteredFoodList, getContext());

        // 어댑터를 RecyclerView에 설정
        categoryItemsRecyclerView.setAdapter(foodAdapter);

        // FirebaseAuth 초기화
        auth = FirebaseAuth.getInstance();

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
                        loadFoods();
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

        setupSearch();

        return view;
    }

    // 카테고리별 데이터 로드
    private void loadFoods() {
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

                    // 필터링된 카테고리별로 데이터를 처리
                    if (food != null && food.getCategories() != null && food.getCategories().contains(currentCategory)) {
                        foodList.add(food);
                    }
                }
                filteredFoodList.clear();
                filteredFoodList.addAll(foodList);
                foodAdapter.notifyDataSetChanged(); // 데이터가 바뀌면 RecyclerView 업데이트
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void filter(String text) {
        filteredFoodList.clear();
        for (Food food : foodList) {
            if (food.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredFoodList.add(food);
            }
        }
        foodAdapter.notifyDataSetChanged();
    }
}
