package com.example.tourmatefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tourmatefirebase.viewmodels.LocationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient providerClient;
    private LocationViewModel locationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        providerClient = LocationServices.getFusedLocationProviderClient(this);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);


        if (isLocationPermissionGranted()) {
            getUserCurrentLocation();
        } else {
            requestLocationPermissionFromUser();
        }
    }

    private boolean isLocationPermissionGranted() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissionFromUser() {
        final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissions(permissions, 111);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Accepted", Toast.LENGTH_SHORT).show();
            getUserCurrentLocation();
        } else {
            Toast.makeText(this, "Denied by user", Toast.LENGTH_SHORT).show();

        }
    }

    private void getUserCurrentLocation() {
        providerClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            return;
                        }
                        locationViewModel.setNewLocation(location);

                    }
                });
    }

}