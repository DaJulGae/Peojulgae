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
    private int originalPrice;
    private int discountedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_pay);

        // Intent에서 원래 가격과 할인된 가격을 가져옴
        Intent intent = getIntent();
        originalPrice = intent.getIntExtra("original_price", 0);
        discountedPrice = intent.getIntExtra("discounted_price", 0);
        int discountAmount = originalPrice - discountedPrice;

        // UI에 가격 정보 반영
        TextView discountTextView = findViewById(R.id.take_text27);
        discountTextView.setText(String.format("-%,d원", discountAmount));

        TextView originalPriceTextView = findViewById(R.id.take_text22);
        originalPriceTextView.setText(String.format("%,d 원", originalPrice));

        TextView totalTextView = findViewById(R.id.take_text29);
        totalTextView.setText(String.format("%,d 원", discountedPrice));

        // 결제 버튼을 클릭하면 Bootpay 결제 시작
        Button payButton = findViewById(R.id.take_pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBootpayPayment();
            }
        });
    }

    private void startBootpayPayment() {
        BootUser user = new BootUser().setPhone("010-1234-5678"); // 사용자 정보 설정

        BootExtra extra = new BootExtra()
                .setCardQuota("0,2,3"); // 할부 설정 (일시불, 2개월, 3개월)

        List<BootItem> items = new ArrayList<>();
        BootItem item = new BootItem()
                .setId("item_1234") // 고유한 상품 ID 추가
                .setName("결제 아이템")
                .setQty(1) // 수량 추가
                .setPrice((double) discountedPrice); // 할인된 금액 설정
        items.add(item);

        Payload payload = new Payload();
        payload.setApplicationId("665095dd51b4276f9e1e11b7")
                .setOrderName("Peojulgae 앱 결제")
                .setOrderId("1234")
                .setPrice((double) discountedPrice) // 할인된 금액 사용
                .setUser(user)
                .setExtra(extra)
                .setItems(items);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("1", "메타데이터1");
        metadata.put("2", "메타데이터2");
        payload.setMetadata(metadata);

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
                        return true; // 결제를 진행할 때 true 반환
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("bootpay", "done: " + data);
                    }
                }).requestPayment();
    }
}