package com.example.a306simulation1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AMap.OnMarkerClickListener {

    private static final int REQUEST_PERMISSIONS = 2;
    private MapView mapView;
    private AMap aMap;
    private Button btnTakePhoto;
    private final List<PhotoMarker> photoMarkers = new ArrayList<>();
    private MapHelper mapHelper;
    private ActivityResultLauncher<Intent> takePictureLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnTakePhoto.setOnClickListener(v -> dispatchTakePictureIntent());

        setupActivityResultLauncher();
        initMap();
        checkPermissions();

        mapHelper = new MapHelper(aMap, this);
    }

    private void setupActivityResultLauncher() {
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");

                        handlePhotoResult(imageBitmap);
                    }
                }
        );
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        } else {
            // No camera app found, create a mock image for testing
            Toast.makeText(this, "No camera app found, using mock image", Toast.LENGTH_SHORT).show();
            Bitmap mockBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mockBitmap);
            canvas.drawColor(Color.BLUE); // simple blue image
            handlePhotoResult(mockBitmap);
        }
    }

    private void handlePhotoResult(Bitmap imageBitmap) {
        if (aMap != null) {
            Location location = aMap.getMyLocation();
            if (location != null) {
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                PhotoMarker photoMarker = new PhotoMarker(currentLatLng, imageBitmap);
                photoMarkers.add(photoMarker);
                addMarkerToMap(photoMarker);

                Toast.makeText(this, "Photo marker added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(this);

            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
        }
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionList.toArray(new String[0]), REQUEST_PERMISSIONS);
        }
    }

    private void addMarkerToMap(PhotoMarker photoMarker) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(photoMarker.getLatLng())
                .title("Building Photo")
                .snippet("Click for details")
                .icon(BitmapDescriptorFactory.fromBitmap(photoMarker.getThumbnail()));

        Marker marker = aMap.addMarker(markerOptions);
        marker.setObject(photoMarker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        PhotoMarker photoMarker = (PhotoMarker) marker.getObject();
        if (photoMarker != null) {
            marker.showInfoWindow();
            return true;
        }
        return false;
    }

    // Map lifecycle
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Some permissions denied", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initMap();
        }
    }
}
