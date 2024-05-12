package com.example.medicineorganizer.recyclerVies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicineorganizer.R;

import java.util.Collection;
import java.util.List;

import dto.NotificationDto;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder> {

    public List<NotificationDto> getStorage() {
        return storage;
    }

    public void setStorage(List<NotificationDto> storage) {
        this.storage = storage;
    }

    public void setStorage(Collection<NotificationDto> storage) {
        this.storage = (List<NotificationDto>) storage;
    }

    private List<NotificationDto> storage;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public HistoryRecyclerViewAdapter(Context context, List<NotificationDto> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.storage = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.history_in_view_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationDto notificationDto = storage.get(position);
        String notificationText = String.format("Препарат: %s,\nПринял пользователь: %s,\nВ количестве: %s",
                notificationDto.getName(), notificationDto.getUsername(), notificationDto.getAmount());
        holder.historyName.setText(notificationText);
    }

    @Override
    public int getItemCount() {
        return storage.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView historyName;
        ImageView historyImage;
        ViewHolder(View itemView) {
            super(itemView);
            historyName = itemView.findViewById(R.id.viewPageHistoryName);
            historyImage = itemView.findViewById(R.id.viewPageHistoryImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    NotificationDto getItem(int id) {
        return storage.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
