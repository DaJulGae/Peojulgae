package com.example.peojulgae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootExtra;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;

public class TakePayActivity extends AppCompatActivity {
    private int discountedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_pay);

        // FoodListActivity에서 전달된 할인된 가격 받기
        Intent intent = getIntent();
        discountedPrice = intent.getIntExtra("discounted_price", 0); // 할인된 가격 받아오기

        // TextView에 할인된 가격 표시
        TextView priceTextView = findViewById(R.id.take_text22); // 결제 금액 표시할 TextView ID 사용
        priceTextView.setText(String.format("%,d 원", discountedPrice)); // 금액 표시 형식

        // 결제 버튼 클릭 리스너 설정
        Button testButton = findViewById(R.id.take_pay_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentTest(v); // 결제 수행
            }
        });
    }

    public void PaymentTest(View v) {
        BootUser user = new BootUser().setPhone("010-9135-3534"); // 구매자 정보

        BootExtra extra = new BootExtra()
                .setCardQuota("0,2,3"); // 일시불, 2개월, 3개월 할부 허용

        List<BootItem> items = new ArrayList<>();
        BootItem item = new BootItem()
                .setId("item_1234")  // 상품 고유 ID
                .setName("결제 아이템")  // 상품 이름
                .setQty(1)  // 상품 수량
                .setPrice((double) discountedPrice); // 할인된 금액
        items.add(item);

        Payload payload = new Payload();
        payload.setApplicationId("5b8f6a4d396fa665fdc2b5e8")
                .setOrderName("Peojulgae 앱 결제")
                .setOrderId("1234")
                .setPrice((double) discountedPrice) // 할인된 금액 설정
                .setUser(user)
                .setExtra(extra)
                .setItems(items);  // BootItem 추가

        Map<String, Object> map = new HashMap<>();
        map.put("1", "abcdef");
        map.put("2", "abcdef55");
        map.put("3", 1234);
        payload.setMetadata(map);

        Bootpay.init(getSupportFragmentManager(), getApplicationContext())
                .setPayload(payload)
                .setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                        Log.d("bootpay", "cancel: " + data);
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("bootpay", "error: " + data);
                    }

                    @Override
                    public void onClose() {
                        Log.d("bootpay", "close");
                        Bootpay.removePaymentWindow();
                    }

                    @Override
                    public void onIssued(String data) {
                        Log.d("bootpay", "issued: " + data);
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("bootpay", "confirm: " + data);
                        return true; // 재고가 있어서 결제를 진행하려 할 때 true 반환
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("done", data);
                    }
                }).requestPayment();
    }
}