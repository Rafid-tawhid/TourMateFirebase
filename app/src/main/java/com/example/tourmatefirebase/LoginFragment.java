package com.example.tourmatefirebase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourmatefirebase.databinding.FragmentLoginBinding;
import com.example.tourmatefirebase.viewmodels.LoginViewModel;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentLoginBinding.inflate(inflater);
        loginViewModel=new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        loginViewModel.authenticationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<LoginViewModel.Authentication>() {
            @Override
            public void onChanged(LoginViewModel.Authentication authentication) {


                if (authentication==LoginViewModel.Authentication.AUTHENTICATED)
                {

                    Navigation.findNavController(container).navigate(R.id.action_loginFragment_to_homeFragment);


                }

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel.errMsg.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.errmsgTV.setText(s);

            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email=binding.emailET.getText().toString();
                final String pass=binding.passET.getText().toString();
                if(email.isEmpty())
                {
                    binding.emailET.setError("Required");
                }
                if (pass.isEmpty())
                {
                    binding.passET.setError("Required");
                }

                loginViewModel.login(email,pass);
                Toast.makeText(requireContext(),"Login Succesfully ",Toast.LENGTH_SHORT).show();

            }
        });
        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String email=binding.emailET.getText().toString();
                final String pass=binding.passET.getText().toString();
                if(email.isEmpty())
                {
                    binding.emailET.setError("Required");
                }
                if (pass.isEmpty()||pass.length()<6)
                {
                    binding.passET.setError("Password must be greater than 6");
                }


                loginViewModel.register(email,pass);
                Toast.makeText(requireContext(),"Registration Succesful",Toast.LENGTH_SHORT).show();


            }
        });
    }
}