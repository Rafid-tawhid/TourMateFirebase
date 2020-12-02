package com.example.tourmatefirebase.repos;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.tourmatefirebase.models.ExpensesModel;
import com.example.tourmatefirebase.models.TourModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    private FirebaseFirestore db;
    private Context context;
    private final String COLLECTION_TOUR="Tours";
    private final String COLLECTION_EXPENSES="Expenses";


    public FirebaseRepository() {

        db=FirebaseFirestore.getInstance();

    }

    public void addNewTour(TourModel tourModel)
    {
      final DocumentReference docRef= db.collection(COLLECTION_TOUR).document();
      tourModel.setId(docRef.getId());

      docRef.set(tourModel).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {

          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {

          }
      });
    }


    public void addNewExpenses(ExpensesModel expensesModel)
    {
        final DocumentReference docRef= db.collection(COLLECTION_EXPENSES).document();
        expensesModel.expId=docRef.getId();

        docRef.set(expensesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public MutableLiveData<List<TourModel>> getAllTours ()
    {
        MutableLiveData<List<TourModel>> tourModelListLiveData=new MutableLiveData<>();
        db.collection(COLLECTION_TOUR).get().addOnCompleteListener(task -> {
            List<TourModel> tourList=new ArrayList<>();
            for (QueryDocumentSnapshot q: task.getResult())
            {
                tourList.add(q.toObject(TourModel.class));
            }

            tourModelListLiveData.postValue(tourList);

        }).addOnFailureListener(e -> {

        });

        return tourModelListLiveData;
    }

    public MutableLiveData<TourModel> getTourById(String id)
    {
        MutableLiveData<TourModel> tourModelMutableLiveData=new MutableLiveData<>();

        db.collection(COLLECTION_TOUR).document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error!=null)
                {
                    return;
                }
                tourModelMutableLiveData.postValue(value.toObject(TourModel.class));

            }
        });

        return tourModelMutableLiveData;

    }
    public MutableLiveData<List<ExpensesModel>> getExpensesByTourId (String tourId)
    {
        MutableLiveData<List<ExpensesModel>> expensesModelMutableLiveData=new MutableLiveData<>();

        db.collection(COLLECTION_EXPENSES).whereEqualTo("tourId",tourId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error !=null)
                {
                return;
                }

                expensesModelMutableLiveData.postValue(value.toObjects(ExpensesModel.class));

            }
        });


        return expensesModelMutableLiveData;
    }


    public void tourDelete(String id)
    {
    WriteBatch batch=db.batch();
        db
                .collection(COLLECTION_EXPENSES).whereEqualTo("tourid",id).get().addOnCompleteListener(task ->
        {
            for (QueryDocumentSnapshot q: task.getResult())
            {
                final ExpensesModel expensesModel=q.toObject(ExpensesModel.class);
                batch.delete(db.collection(COLLECTION_EXPENSES).document(expensesModel.expId));



            }
            batch.delete(db.collection(COLLECTION_TOUR).document(id));
            batch.commit();
        });
    }
}
