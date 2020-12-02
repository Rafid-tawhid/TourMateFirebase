package com.example.tourmatefirebase.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tourmatefirebase.models.ExpensesModel;
import com.example.tourmatefirebase.models.TourModel;
import com.example.tourmatefirebase.repos.FirebaseRepository;

import java.util.List;

public class TourViewModel extends AndroidViewModel {

    private FirebaseRepository repository;
    public TourViewModel(@NonNull Application application) {
        super(application);

        repository=new FirebaseRepository();

    }
    public void addNewTour(TourModel tourModel)
    {
       repository.addNewTour(tourModel);
    }
    public void addNewExp(ExpensesModel expensesModel)
    {
       repository.addNewExpenses(expensesModel);
    }

    public MutableLiveData<List<TourModel>> fetchTours()
    {
        return repository.getAllTours();
    }
    public MutableLiveData<TourModel> fetchTourById(String id)
    {
        return repository.getTourById(id);
    }
    public MutableLiveData<List<ExpensesModel>> fetchExpensesById(String tourId)
    {
        return repository.getExpensesByTourId(tourId);
    }

    public double getTotalExpensesAmount(List<ExpensesModel> expensesModelList)
    {
        double total =0.0;
        for (ExpensesModel expensesModel:expensesModelList)
        {
            total +=expensesModel.amount;
        }
        return total;
    }

    public void removeTour(String id)
    {
        repository.tourDelete(id);
    }
}
