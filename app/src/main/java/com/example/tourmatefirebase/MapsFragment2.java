package com.example.tourmatefirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmatefirebase.viewmodels.LocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment2 extends Fragment {
    private LocationViewModel locationViewModel;
    private GoogleMap map;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            map=googleMap;
            locationViewModel.locationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Location>() {
                @Override
                public void onChanged(Location location) {




                    locationViewModel=new ViewModelProvider(requireActivity()).get(LocationViewModel.class);

                    LatLng sydney = new LatLng(-23,90);
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("I am here"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f));
                }
            });





                }





    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        locationViewModel=new ViewModelProvider(this).get(LocationViewModel.class);
        return inflater.inflate(R.layout.fragment_maps2, container, false);
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
}