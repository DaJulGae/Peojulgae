package fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.peojulgae.AddmartActivity;
import com.example.peojulgae.AddproductActivity;
import com.example.peojulgae.AddrestaurantActivity;
import com.example.peojulgae.CancelPayActivity;
import com.example.peojulgae.ManageFoodActivity;
import com.example.peojulgae.R;
import com.example.peojulgae.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class Frag4 extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private View view;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button loginButton, registerButton, logoutButton, addrestaurantButton, addmartButton;
    private ImageButton addButton, editButton;
    private EditText emailEditText, passwordEditText;
    private ImageView profileImageView, profile_sellerImageView1;
    private LinearLayout loginLayout, buyerLayout, sellerLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mypagefragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        profileImageView = view.findViewById(R.id.profile_ImageView);
        profile_sellerImageView1 = view.findViewById(R.id.profile_sellerImageView1);
        loginButton = view.findViewById(R.id.login_Button);
        registerButton = view.findViewById(R.id.register_Button);
        emailEditText = view.findViewById(R.id.email_edittext);
        passwordEditText = view.findViewById(R.id.password_edittext);
        logoutButton = view.findViewById(R.id.logout_Button);
        addButton = view.findViewById(R.id.add);
        editButton = view.findViewById(R.id.edit);
        addrestaurantButton = view.findViewById(R.id.restaurant);
        addmartButton = view.findViewById(R.id.mart);
        loginLayout = view.findViewById(R.id.login_layout);
        buyerLayout = view.findViewById(R.id.buyer_layout);
        sellerLayout = view.findViewById(R.id.seller_layout);

        // TextView 정의 및 클릭 리스너 설정
        TextView paymentTextView = view.findViewById(R.id.OrderManagement);
        paymentTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CancelPayActivity.class);
            startActivity(intent);
        });

        TextView paymentTextView2 = view.findViewById(R.id.OrderManagement2);
        paymentTextView2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CancelPayActivity.class);
            startActivity(intent);
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        // 사용자가 로그인이 안되어있을때 로그인 화면 보여줌
        if (currentUser == null) {
            showLoginLayout();
        } else {
            showUserLayout(currentUser);
        }

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddproductActivity.class);
            startActivity(intent);
        });

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManageFoodActivity.class);
            startActivity(intent);
        });
        addrestaurantButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddrestaurantActivity.class);
            startActivity(intent);
        });
        addmartButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddmartActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void showLoginLayout() {
        loginLayout.setVisibility(View.VISIBLE);
        profileImageView.setVisibility(View.GONE);
        profile_sellerImageView1.setVisibility(View.GONE);
        buyerLayout.setVisibility(View.GONE);
        sellerLayout.setVisibility(View.GONE);
        logoutButton.setVisibility(View.GONE);
        loginButton.setOnClickListener(v -> loginUser());

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
    }

    private void showUserLayout(FirebaseUser currentUser) {
        loginLayout.setVisibility(View.GONE);
        logoutButton.setVisibility(View.VISIBLE);
        profileImageView.setVisibility(View.VISIBLE);
        profile_sellerImageView1.setVisibility(View.VISIBLE);
        checkUserRole(currentUser.getUid());

        profileImageView.setOnClickListener(v -> openFileChooser());
        profile_sellerImageView1.setOnClickListener(v -> openFileChooser());

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            showLoginLayout();
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    showUserLayout(user);
                }
            } else {
                Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRole(String userId) {
        mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);
                    if ("buyer".equals(role)) {
                        showBuyerUI();
                    } else if ("seller".equals(role)) {
                        String restaurantId = snapshot.child("restaurantId").getValue(String.class);
                        if (restaurantId != null) {
                            showSellerUI(restaurantId);
                        } else {
                            showSellerUI(null);
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "사용자 정보를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "데이터베이스 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBuyerUI() {
        buyerLayout.setVisibility(View.VISIBLE);
        sellerLayout.setVisibility(View.GONE);
    }

    private void showSellerUI(String restaurantId) {
        buyerLayout.setVisibility(View.GONE);
        sellerLayout.setVisibility(View.VISIBLE);
        if (restaurantId != null) {
            addButton.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), com.example.peojulgae.AddproductActivity.class);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
            });
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
