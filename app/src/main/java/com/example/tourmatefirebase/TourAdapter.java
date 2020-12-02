package com.example.tourmatefirebase;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmatefirebase.models.TourModel;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder>{

    private List<TourModel> tourModels;
    private Context context;


    public TourAdapter(List<TourModel> tourModels, Context context) {
        this.tourModels = tourModels;
        this.context = context;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View itemView= LayoutInflater.from(context).inflate(R.layout.tour_row,parent,false);
        return new TourViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {

        holder.nameTv.setText(tourModels.get(position).getName());
        holder.dateTV.setText(tourModels.get(position).getFormatedDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle=new Bundle();
                bundle.putString("id",tourModels.get(position).getId());

                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_tourDetailsFragment,bundle);


            }
        });



    }

    @Override
    public int getItemCount() {
        return tourModels.size();
    }

    public class TourViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, dateTV, remaining;

        public TourViewHolder(@NonNull View itemView) {

            super(itemView);

            nameTv = itemView.findViewById(R.id.row_tour_name);
            dateTV = itemView.findViewById(R.id.row_tour_date);
            remaining = itemView.findViewById(R.id.row_tour_remaining);
        }



    }
}
