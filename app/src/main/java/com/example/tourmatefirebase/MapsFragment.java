package com.example.tourmatefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourmatefirebase.viewmodels.LocationViewModel;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements GoogleMap.OnMapLongClickListener {
    private LocationViewModel locationViewModel;
    private GoogleMap map;
    private GeofencingClient geofencingClient;
    private List<Geofence> geofenceList = new ArrayList<>();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {


            map = googleMap;
            map.setOnMapLongClickListener(MapsFragment.this::onMapLongClick);
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            map.getUiSettings().setZoomControlsEnabled(true);
            map.getUiSettings().isCompassEnabled();
            map.getUiSettings().isMapToolbarEnabled();
            map.getUiSettings().isMyLocationButtonEnabled();
            map.getUiSettings().isMyLocationButtonEnabled();


//            locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);

            locationViewModel.locationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    LatLng dhaka = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().position(dhaka).title("I am here"));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(dhaka, 15f));
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        locationViewModel=new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        geofencingClient = LocationServices.getGeofencingClient(getActivity());
        return inflater.inflate(R.layout.fragment_maps, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        createGeofence(latLng);
    }

    private boolean isLocationPermissionGranted() {
        return getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void createGeofence(LatLng latLng) {

        geofenceList.add(new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("Destination")

                .setCircularRegion(
                        latLng.latitude,
                        latLng.longitude,
                        200
                )
                .setExpirationDuration(12 * 60 * 60 * 1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());

        if (isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }


            geofencingClient.addGeofences(getGeofencingRequest(),getGeofencePendingIntent()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(), "Geofence Added Succesfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                }
            });
        }


    }
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.

        PendingIntent geofencePendingIntent=null;
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        geofencePendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }
}