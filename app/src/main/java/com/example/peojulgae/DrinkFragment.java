package com.example.peojulgae;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DrinkFragment extends Fragment {

    private RecyclerView categoryItemsRecyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private List<Food> filteredFoodList;
    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_drink_fragment, container, false);  // Ensure this matches your layout file name

        categoryItemsRecyclerView = view.findViewById(R.id.drinkRecyclerView);
        categoryItemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        foodList = new ArrayList<>();
        filteredFoodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(filteredFoodList, getContext());

        // 어댑터를 RecyclerView에 설정
        categoryItemsRecyclerView.setAdapter(foodAdapter);

        dbRef = FirebaseDatabase.getInstance().getReference("Foods");

        loadFoods();

        return view;
    }

    private void loadFoods() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodList.clear();
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    Food food = foodSnapshot.getValue(Food.class);
                    if (food != null && food.getCategories().contains("음료")) {
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
}
