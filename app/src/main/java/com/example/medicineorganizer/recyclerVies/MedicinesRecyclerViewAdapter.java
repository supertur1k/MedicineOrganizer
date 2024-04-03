package com.example.medicineorganizer.recyclerVies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicineorganizer.R;

import java.util.List;

import dto.Medicament;

public class MedicinesRecyclerViewAdapter extends RecyclerView.Adapter<MedicinesRecyclerViewAdapter.ViewHolder> {

    public List<Medicament> getStorage() {
        return storage;
    }

    public void setStorage(List<Medicament> storage) {
        this.storage = storage;
    }

    private List<Medicament> storage;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    // data is passed into the constructor
    public MedicinesRecyclerViewAdapter(Context context, List<Medicament> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.storage = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.medicament_in_view_design, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Medicament medicament = storage.get(position);
        holder.medicamentName.setText(medicament.getName());
        //holder.firstAidKitImage.setImageResource(R.drawable.first_aid_kit_image);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return storage.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView medicamentName;
        ImageView medicamentImage;

        ViewHolder(View itemView) {
            super(itemView);
            medicamentName = itemView.findViewById(R.id.viewPageMedicamentName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Medicament getItem(int id) {
        return storage.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
