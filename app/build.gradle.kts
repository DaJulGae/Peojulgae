plugins {
    alias(libs.plugins.androidApplication)
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.peojulgae"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.peojulgae"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.android.v260)
    implementation(libs.v2.all) // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation(libs.v2.user) // 카카오 로그인 API 모듈
    implementation(libs.v2.share) // 카카오톡 공유 API 모듈
    implementation(libs.v2.talk) // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation(libs.v2.friend) // 피커 API 모듈
    implementation(libs.v2.navi) // 카카오내비 API 모듈
    implementation (libs.v2.cert) // 카카오톡 인증 서비스 API 모듈


    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)


    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.appcompat)
    implementation(libs.firebase.database)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.appcheck.playintegrity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth:21.0.1")

    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

    implementation (libs.oauth)
    implementation (libs.kotlin.stdlib)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.appcompat.v131)
    implementation (libs.legacy.support.core.utils)
    implementation (libs.browser)
    implementation (libs.security.crypto)
    implementation (libs.core.ktx)
    implementation (libs.fragment.ktx)
    implementation (libs.lifecycle.viewmodel.ktx)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.moshi.kotlin)
    implementation (libs.logging.interceptor)
    implementation (libs.lottie)

    implementation ("io.github.bootpay:android:4.4.3")
    implementation ("io.github.bootpay:android-bio:4.4.21")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}