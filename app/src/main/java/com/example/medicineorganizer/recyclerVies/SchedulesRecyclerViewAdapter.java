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

import java.util.Collection;
import java.util.List;

import dto.Medicament;
import dto.ScheduleCreateRequestDTO;
import dto.ScheduleCreateResponseDTO;

public class SchedulesRecyclerViewAdapter extends RecyclerView.Adapter<SchedulesRecyclerViewAdapter.ViewHolder> {

    public List<ScheduleCreateResponseDTO> getStorage() {
        return storage;
    }

    public void setStorage(List<ScheduleCreateResponseDTO> storage) {
        this.storage = storage;
    }

    public void setStorage(Collection<ScheduleCreateResponseDTO> storage) {
        this.storage = (List<ScheduleCreateResponseDTO>) storage;
    }

    private List<ScheduleCreateResponseDTO> storage;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    // data is passed into the constructor
    public SchedulesRecyclerViewAdapter(Context context, List<ScheduleCreateResponseDTO> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.storage = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.schedule_in_view_design, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleCreateResponseDTO schedule = storage.get(position);
        holder.scheduleName.setText(schedule.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return storage.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView scheduleName;
        ImageView scheduleImage;

        ViewHolder(View itemView) {
            super(itemView);
            scheduleName = itemView.findViewById(R.id.viewPageScheduleName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    ScheduleCreateResponseDTO getItem(int id) {
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
