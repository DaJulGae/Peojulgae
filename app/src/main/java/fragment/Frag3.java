// Frag3.java
package fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peojulgae.Mart;
import com.example.peojulgae.MartAdapter;
import com.example.peojulgae.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Frag3 extends Fragment {

    private RecyclerView groceryRecyclerView;
    private MartAdapter martAdapter;
    private List<Mart> martList;
    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // activity_grocery_store 레이아웃을 인플레이트
        View view = inflater.inflate(R.layout.activity_grocery_store, container, false);

        // UI 요소 초기화
        groceryRecyclerView = view.findViewById(R.id.mart_recycler_view); // RecyclerView 참조
        groceryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1)); // 1열 레이아웃

        // RecyclerView 및 데이터 초기화
        martList = new ArrayList<>();
        martAdapter = new MartAdapter(getContext(), martList);

        // 어댑터를 RecyclerView에 설정
        groceryRecyclerView.setAdapter(martAdapter);

        // Firebase 데이터베이스 참조 설정 (모든 마트의 데이터를 가져옴)
        dbRef = FirebaseDatabase.getInstance().getReference().child("marts");

        // 마트 데이터 로드
        loadAllMarts();

        return view;
    }

    private void loadAllMarts() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                martList.clear();
                for (DataSnapshot martSnapshot : snapshot.child("Marts").getChildren()) {
                    try {
                        Mart mart = martSnapshot.getValue(Mart.class);
                        if (mart != null) {
                            mart.setMartId(martSnapshot.getKey()); // 마트 ID 설정
                            martList.add(mart);
                        }
                    } catch (Exception e) {
                        Log.e("Frag3", "마트 데이터 파싱 오류: " + e.getMessage());
                    }
                }
                // 어댑터에 데이터 변경 알림
                martAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "데이터 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
