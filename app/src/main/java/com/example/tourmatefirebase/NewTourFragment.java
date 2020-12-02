package com.example.tourmatefirebase;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.tourmatefirebase.databinding.FragmentNewTourBinding;
import com.example.tourmatefirebase.models.TourModel;
import com.example.tourmatefirebase.viewmodels.TourViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NewTourFragment extends Fragment {

    private FragmentNewTourBinding binding;
    private String formatedDate;
    private long timeSnap;
    private int selectedMonth;
    private int selectedYear;
    private TourViewModel tourViewModel;
 

    public NewTourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tourViewModel= new ViewModelProvider(requireActivity()).get(TourViewModel.class);
        binding=FragmentNewTourBinding.inflate(inflater);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

binding.inputDate.setOnClickListener(v -> {

    getDateFromUser();

});
        binding.saveBtn.setOnClickListener(v -> {

            final  String name=binding.inputTourname.getText().toString();
            final  String destination=binding.inputDestination.getText().toString();
            final  String budget=binding.inputBudget.getText().toString();

            if(name.isEmpty())
            {
                binding.inputTourname.setError("Required");
            }
            if(destination.isEmpty())
            {
                binding.inputDestination.setError("Required");
            }
            if(budget.isEmpty())
            {
                binding.inputBudget.setError("Required");
            }

            else
            {

                final TourModel tourModel=new TourModel(null,name,destination,Double.parseDouble(budget),formatedDate,timeSnap,selectedMonth,selectedYear);

                tourViewModel.addNewTour(tourModel);
                Navigation.findNavController(v).navigate(R.id.action_newTourFragment_to_homeFragment);

            }
        });
    }

    private void getDateFromUser() {

        final Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),listener,year,month,day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }
    private DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            final Calendar calendar=Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            final SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            formatedDate= sdf.format(calendar.getTime());
            timeSnap=calendar.getTimeInMillis();
            selectedMonth=calendar.get(Calendar.MONTH);
            selectedYear=calendar.get(Calendar.YEAR);
           binding.inputDate.setText(formatedDate);

//           final Date currentDate=new Date();
//           final Date selectDate=new Date(timeSnap);

            final long currentDate=System.currentTimeMillis();
            final long periodSeconds = (timeSnap - currentDate) / 1000;
            final long elapsedDays = periodSeconds / 60 / 60 / 24;


            final Bundle bundle=new Bundle();
            bundle.putString("rem", String.valueOf(elapsedDays));


        }
    };
}