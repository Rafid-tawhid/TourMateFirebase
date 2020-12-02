package com.example.tourmatefirebase;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourmatefirebase.databinding.FragmentTourDetailsBinding;
import com.example.tourmatefirebase.models.ExpensesModel;
import com.example.tourmatefirebase.models.TourModel;
import com.example.tourmatefirebase.viewmodels.TourViewModel;
import com.google.android.gms.common.internal.ICancelToken;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Objects;


public class TourDetailsFragment extends Fragment {

    private FragmentTourDetailsBinding binding;
    private TourViewModel tourViewModel;
    private String tourId;

    public TourDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tourViewModel= new ViewModelProvider(requireActivity()).get(TourViewModel.class);
        binding=FragmentTourDetailsBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle bundle=getArguments();
        if (bundle!=null)
        {
            tourId=bundle.getString("id");
            tourViewModel.fetchTourById(tourId).observe(getViewLifecycleOwner(), new Observer<TourModel>() {
                @Override
                public void onChanged(TourModel tourModel) {
                    binding.totalAmountTV.setText(String.valueOf(tourModel.getBudget()));

                    getExpenseByTourId(tourModel);


                }
            });

        }




        binding.addExpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialoge();
            }
        });
    }

    private void getExpenseByTourId(TourModel tourModel) {
    tourViewModel.fetchExpensesById(tourId).observe(getViewLifecycleOwner(), new Observer<List<ExpensesModel>>() {

        @SuppressLint("ResourceAsColor")
        @Override
        public void onChanged(List<ExpensesModel> expensesModels) {
            //show expenses in recyclerView

            binding.totalExpId.setText(String.valueOf(tourViewModel.getTotalExpensesAmount(expensesModels)));
            double b=tourModel.getBudget();
            double e=tourViewModel.getTotalExpensesAmount(expensesModels);

            String s = String.valueOf((tourModel.getBudget())-(tourViewModel.getTotalExpensesAmount(expensesModels)));
            if (b>e)
            {
                binding.rowTourRemaining.setText(s);
            }
       else
            {
                binding.rowTourRemaining.setTextColor(R.color.red);
                binding.rowTourRemaining.setText(s);
            }



        }
    });
    }


    private void showAlertDialoge() {

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Expenses");
        final View layout=LayoutInflater.from(getActivity()).inflate(R.layout.add_exp_layout,null,true);
        builder.setView(layout);

        final EditText titelET=layout.findViewById(R.id.add_exp_titel);
        final EditText amountET=layout.findViewById(R.id.add_exp_amount);
        final Button addBtn=layout.findViewById(R.id.btn_save);
        final Button cancelBtn=layout.findViewById(R.id.btn_cancel);

        AlertDialog dialog=builder.create();
        dialog.show();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String titel=titelET.getText().toString();
                final String amount=amountET.getText().toString();
                final long time=System.currentTimeMillis();
                final ExpensesModel expensesModel=new ExpensesModel(null,tourId,titel,Double.parseDouble(amount),time);
                tourViewModel.addNewExp(expensesModel);
             
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

    }


    //Options
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tourdetails_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.editId:



                break;


            case R.id.deleteId:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Do you really want to delete the Tour ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tourViewModel.removeTour(tourId);
                        Navigation.findNavController(requireView()).navigate(R.id.action_tourDetailsFragment_to_homeFragment);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                });

                builder.create().show();


                break;
        }

        return false;
    }

}