package com.example.peojulgae;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.TrackingManager;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements KakaoMap.OnLabelClickListener {
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private final String[] locationPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng startPosition = null;
    private ProgressBar progressBar;
    private MapView mapView;
    private Label centerLabel;
    private boolean requestingLocationUpdates = false;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private KakaoMap kakaoMap;
    private Map<String, Label> labelMap = new HashMap<>();

    private KakaoMapReadyCallback readyCallback = new KakaoMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull KakaoMap map) {
            progressBar.setVisibility(View.GONE);
            kakaoMap = map;
            LabelLayer layer = kakaoMap.getLabelManager().getLayer();
            centerLabel = layer.addLabel(LabelOptions.from("centerLabel", startPosition)
                    .setStyles(LabelStyle.from(R.drawable.mylocation).setAnchorPoint(0.5f, 0.5f))
                    .setRank(1));
            TrackingManager trackingManager = kakaoMap.getTrackingManager();
            trackingManager.startTracking(centerLabel);
            startLocationUpdates();
            addMarkers(kakaoMap);

            trackingManager.stopTracking(); // 트래킹을 중지하여 자동 복귀를 막음

            kakaoMap.setOnLabelClickListener(MapActivity.this);
        }

        @NonNull
        @Override
        public LatLng getPosition() {
            return startPosition;
        }

        @NonNull
        @Override
        public int getZoomLevel() {
            return 17;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.map_view);
        progressBar = findViewById(R.id.progressBar);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L).build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    centerLabel.moveTo(LatLng.from(location.getLatitude(), location.getLongitude()));
                }
            }
        };

        if (ContextCompat.checkSelfPermission(this, locationPermissions[0]) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, locationPermissions[1]) == PackageManager.PERMISSION_GRANTED) {
            getStartLocation();
        } else {
            ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @SuppressLint("MissingPermission")
    private void getStartLocation() {
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        startPosition = LatLng.from(location.getLatitude(), location.getLongitude());
                        mapView.start(readyCallback);
                    }
                });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        requestingLocationUpdates = true;
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getStartLocation();
            } else {
                showPermissionDeniedDialog();
            }
        }
    }

    private void showPermissionDeniedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("위치 권한 거부시 앱을 사용할 수 없습니다.")
                .setPositiveButton("권한 설정하러 가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            startActivity(intent);
                        } finally {
                            finish();
                        }
                    }
                })
                .setNegativeButton("앱 종료하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void addMarkers(@NonNull KakaoMap kakaoMap) {
        LabelLayer layer = kakaoMap.getLabelManager().getLayer();

        // 첫번째 마커 찍기
        LatLng position1 = LatLng.from(37.588145, 127.096791); // "가나 점보 돈까스"
        Label marker1 = layer.addLabel(LabelOptions.from("specificMarker1", position1)
                .setStyles(LabelStyle.from(R.drawable.red_marker).setAnchorPoint(0.5f, 0.5f))
                .setRank(1));
        labelMap.put("specificMarker1", marker1);

        // 사당초등학교
        LatLng position2 = LatLng.from(37.4735357, 126.9737232); // 사당초등학교
        Label marker2 = layer.addLabel(LabelOptions.from("specificMarker2", position2)
                .setStyles(LabelStyle.from(R.drawable.red_marker).setAnchorPoint(0.5f, 0.5f))
                .setRank(1));
        labelMap.put("specificMarker2", marker2);

        // 지지고
        LatLng position3 = LatLng.from(37.587539, 127.097296); // 지지고
        Label marker3 = layer.addLabel(LabelOptions.from("specificMarker3", position3)
                .setStyles(LabelStyle.from(R.drawable.red_marker).setAnchorPoint(0.5f, 0.5f))
                .setRank(1));
        labelMap.put("specificMarker3", marker3);

        // 알촌
        LatLng position4 = LatLng.from(37.587822, 127.097018); // 알촌
        Label marker4 = layer.addLabel(LabelOptions.from("specificMarker4", position4)
                .setStyles(LabelStyle.from(R.drawable.red_marker).setAnchorPoint(0.5f, 0.5f))
                .setRank(1));
        labelMap.put("specificMarker4", marker4);
    }

    @Override
    public void onLabelClicked(KakaoMap kakaoMap, LabelLayer layer, Label label) {
        for (Map.Entry<String, Label> entry : labelMap.entrySet()) {
            if (entry.getValue().equals(label)) {
                String labelId = entry.getKey();
                switch (labelId) {
                    case "specificMarker1":
                        startBaloonActivity(labelId);
                        break;
                    case "specificMarker3":
                        startGGgoActivity(labelId);
                        break;
                    case "specificMarker4":
                        // 지지고를 클릭했을 때 alchonActivty 실행
                        startalchonActivity(labelId);
                    default:
                        break;
                }
                break;
            }
        }
    }

    // BaloonActivity를 실행하는 메서드
    private void startBaloonActivity(String labelId) {
        Intent intent = new Intent(this, BaloonActivity.class);
        intent.putExtra("labelId", labelId);
        startActivity(intent);
    }

    // GGgoActivity를 실행하는 메서드
    private void startGGgoActivity(String labelId) {
        Intent intent = new Intent(this, GGgoActivity.class);
        intent.putExtra("labelId", labelId);
        startActivity(intent);
    }
    // alchon_activity를 실행하는 메서드
    private void startalchonActivity(String labelId) {
        Intent intent = new Intent(this, alchon_activity.class);
        intent.putExtra("labelId", labelId);
        startActivity(intent);
    }
}