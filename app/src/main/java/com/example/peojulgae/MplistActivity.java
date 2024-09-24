
package com.example.peojulgae;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MplistActivity extends AppCompatActivity {

    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseReference dbRef;
    private String martId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mplist); // activity_product_list 레이아웃 참조

        // 인텐트로부터 마트 ID를 가져옴
        martId = getIntent().getStringExtra("martId");
        if (martId == null) {
            Toast.makeText(this, "마트 ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // RecyclerView 초기화
        productRecyclerView = findViewById(R.id.mplist_recyclerview);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // 리스트 및 어댑터 초기화
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        productRecyclerView.setAdapter(productAdapter);

        // Firebase 데이터베이스 참조 설정
        dbRef = FirebaseDatabase.getInstance().getReference().child("Marts").child(martId).child("Products");


        // 상품 데이터 로드
        loadProducts();
    }

    private void loadProducts() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MplistActivity.this, "상품 로드 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
