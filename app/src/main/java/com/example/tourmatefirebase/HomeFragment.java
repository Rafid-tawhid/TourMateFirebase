package com.example.tourmatefirebase;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourmatefirebase.databinding.FragmentHomeBinding;
import com.example.tourmatefirebase.models.TourModel;
import com.example.tourmatefirebase.viewmodels.LocationViewModel;
import com.example.tourmatefirebase.viewmodels.LoginViewModel;
import com.example.tourmatefirebase.viewmodels.TourViewModel;

import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private TourViewModel tourViewModel;
    private LocationViewModel locationViewModel;
    private FragmentHomeBinding binding;
 



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentHomeBinding.inflate(inflater);

        loginViewModel=new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        tourViewModel=new ViewModelProvider(requireActivity()).get(TourViewModel.class);
        locationViewModel=new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        locationViewModel.locationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                Toast.makeText(getContext(),"lat:"+location.getLatitude()+"lng:"+location.getLongitude(),Toast.LENGTH_SHORT).show();
            }
        });





        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        super.onViewCreated(view, savedInstanceState);

        loginViewModel.authenticationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<LoginViewModel.Authentication>() {
            @Override
            public void onChanged(LoginViewModel.Authentication authentication) {


                if (authentication==LoginViewModel.Authentication.UNAUTHENTICATED)
                {

                    Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);



                }
                else {setHasOptionsMenu(true);
                    tourViewModel.fetchTours().observe(getViewLifecycleOwner(), new Observer<List<TourModel>>() {
                        @Override
                        public void onChanged(List<TourModel> tourModels) {

                            final TourAdapter adapter=new TourAdapter(tourModels,getActivity());
                            final LinearLayoutManager llm=new LinearLayoutManager(getActivity());
                            binding.tourRV.setLayoutManager(llm);
                            binding.tourRV.setAdapter(adapter);

                        }
                    });}


            }
        });




        binding.fab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_newTourFragment));




    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.side_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mapp:

                Navigation.findNavController(getView()).navigate(R.id.mapsFragment3);
//               Navigation.findNavController(getActivity(),R.id.homeFragment).navigate(R.id.tourDetailsFragment);
                break;


            case R.id.logout:
                // do more stuff
                loginViewModel.logout();
                Toast.makeText(getActivity(),"Logging Out",Toast.LENGTH_SHORT).show();

                break;
        }

        return false;
    }


}