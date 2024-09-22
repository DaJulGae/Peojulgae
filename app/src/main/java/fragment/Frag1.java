package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.peojulgae.BaloonActivity;
import com.example.peojulgae.GGgoActivity;
import com.example.peojulgae.MapActivity;
import com.example.peojulgae.alchon_activity;
import com.example.peojulgae.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Frag1 extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefragment, container, false);

        // FirebaseAuth 인스턴스 가져오기
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // TextView 가져오기
        TextView welcomeTextView = view.findViewById(R.id.textView);

        // 로그인된 사용자 정보 가져와서 TextView 업데이트
        if (currentUser != null) {
            String userId = currentUser.getEmail();
            welcomeTextView.setText(userId + "님 환영합니다");
        } else {
            welcomeTextView.setText("로그인된 사용자가 없습니다.");
        }

        // VisitButton 찾아서 클릭 이벤트 설정
        Button visitButton = view.findViewById(R.id.VisitButton);
        visitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MapActivity로 전환
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });

        // 첫 번째 이미지 클릭 시 BaloonActivity로 이동
        ImageView storeImage1 = view.findViewById(R.id.storeImage_1);
        storeImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BaloonActivity.class);
                startActivity(intent);
            }
        });

        // 두 번째 이미지 클릭 시 AlchonActivity로 이동
        ImageView storeImage2 = view.findViewById(R.id.storeImage_2);
        storeImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), alchon_activity.class);
                startActivity(intent);
            }
        });

        // 세 번째 이미지 클릭 시 GGgoActivity로 이동
        ImageView storeImage3 = view.findViewById(R.id.storeImage_3);
        storeImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GGgoActivity.class);
                startActivity(intent);
            }
        });

        // 추가된 부분: 첫 번째 음식 이미지 클릭 시 GGgoActivity로 이동
        ImageView foodImage1 = view.findViewById(R.id.food_image_1);
        foodImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GGgoActivity.class);
                startActivity(intent);
            }
        });

        // 추가된 부분: 두 번째 음식 이미지 클릭 시 BaloonActivity로 이동
        ImageView foodImage2 = view.findViewById(R.id.food_image_2);
        foodImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BaloonActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}