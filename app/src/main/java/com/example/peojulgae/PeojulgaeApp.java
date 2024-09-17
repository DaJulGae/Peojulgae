package com.example.peojulgae;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;

public class PeojulgaeApp extends Application {  // 클래스 이름과 상속 선언 수정
    @Override
    public void onCreate() {
        super.onCreate();

        // FirebaseApp 초기화
        FirebaseApp.initializeApp(this);  // context: this는 필요 없음

        // Play Integrity App Check 초기화
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
        );
    }
}
