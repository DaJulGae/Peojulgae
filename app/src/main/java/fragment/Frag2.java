package fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peojulgae.Category;
import com.example.peojulgae.CategoryAdapter;
import com.example.peojulgae.CategoryItemsFragment;
import com.example.peojulgae.ChickenFragment;
import com.example.peojulgae.ChineseFragment;
import com.example.peojulgae.DessertFragment;
import com.example.peojulgae.DrinkFragment;
import com.example.peojulgae.Food;
import com.example.peojulgae.FoodAdapter;
import com.example.peojulgae.JapaneseFragment;
import com.example.peojulgae.KoreanFragment;
import com.example.peojulgae.PizzaFragment;
import com.example.peojulgae.R;
import com.example.peojulgae.SnackFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Frag2 extends Fragment {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView categoryRecyclerView;
    private RecyclerView searchRecyclerView;
    private FoodAdapter foodAdapter;
    private CategoryAdapter categoryAdapter;
    private List<Food> foodList;
    private List<Category> categoryList;
    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categoryfragment, container, false);

        // 검색 관련 UI
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);

        // 카테고리 RecyclerView
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 검색 결과 RecyclerView (초기에는 숨김)
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchRecyclerView.setVisibility(View.GONE); // 검색 결과는 기본적으로 숨김 상태

        // 파이어베이스 참조
        dbRef = FirebaseDatabase.getInstance().getReference("FoodS");

        // 카테고리 목록 초기화
        setupCategoryRecyclerView();

        // 검색 버튼 클릭 이벤트
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                searchFoods(query); // 검색 실행
            } else {
                Toast.makeText(getContext(), "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // 카테고리 목록을 설정하는 메서드
    private void setupCategoryRecyclerView() {
        // 카테고리 리스트 초기화
        categoryList = new ArrayList<>();
        categoryList.add(new Category("피자"));
        categoryList.add(new Category("치킨"));
        categoryList.add(new Category("한식"));
        categoryList.add(new Category("일식"));
        categoryList.add(new Category("중식"));
        categoryList.add(new Category("디저트"));
        categoryList.add(new Category("음료"));

        // 카테고리 어댑터 설정
        categoryAdapter = new CategoryAdapter(categoryList, getContext(), category -> openCategoryFragment(category.getName()));
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    // 카테고리 선택 시 프래그먼트 전환
    private void openCategoryFragment(String categoryName) {
        Fragment fragment;
        switch (categoryName) {
            case "피자":
                fragment = new PizzaFragment();
                break;
            case "치킨":
                fragment = new ChickenFragment();
                break;
            case "디저트":
                fragment = new DessertFragment();
                break;
            case "음료":
                fragment = new DrinkFragment();
                break;
            case "분식":
                fragment = new SnackFragment();
                break;
            case "일식":
                fragment = new JapaneseFragment();
                break;
            case "중식":
                fragment = new ChineseFragment();
                break;
            case "한식":
                fragment = new KoreanFragment();
                break;
            default:
                fragment = new CategoryItemsFragment(); // 기본 프래그먼트 설정
                break;
        }
        getParentFragmentManager().beginTransaction()
                .replace(R.id.main_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

    // 파이어베이스에서 상품 이름으로 검색하는 메서드
    private void searchFoods(String query) {
        dbRef.orderByChild("name").startAt(query).endAt(query + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> searchResults = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                        Food food = foodSnapshot.getValue(Food.class);
                        if (food != null) {
                            searchResults.add(food); // 검색 결과 추가
                        }
                    }

                    if (!searchResults.isEmpty()) {
                        // 검색 결과가 있으면 카테고리 RecyclerView 숨기고 검색 결과만 표시
                        categoryRecyclerView.setVisibility(View.GONE);
                        searchRecyclerView.setVisibility(View.VISIBLE);

                        // 검색 결과를 표시하는 어댑터 설정
                        foodAdapter = new FoodAdapter(searchResults, getContext());
                        searchRecyclerView.setAdapter(foodAdapter);
                    } else {
                        Toast.makeText(getContext(), "검색 결과가 없습니다", Toast.LENGTH_SHORT).show();
                        searchRecyclerView.setVisibility(View.GONE);
                        categoryRecyclerView.setVisibility(View.VISIBLE); // 검색 결과가 없으면 카테고리 목록 다시 표시
                    }
                } else {
                    Toast.makeText(getContext(), "검색 결과가 없습니다", Toast.LENGTH_SHORT).show();
                    searchRecyclerView.setVisibility(View.GONE);
                    categoryRecyclerView.setVisibility(View.VISIBLE); // 검색 결과가 없으면 카테고리 목록 다시 표시
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
