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

public class NotificationsRecyclerViewAdapter extends RecyclerView.Adapter<NotificationsRecyclerViewAdapter.ViewHolder> {

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

    public NotificationsRecyclerViewAdapter(Context context, List<NotificationDto> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.storage = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.notification_in_view_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationDto notificationDto = storage.get(position);
        String notificationText;
        if (notificationDto.getName().equals("Приглашение в аптечку")) {
            notificationText = notificationDto.getName();
            int resourceId = layoutInflater.getContext().getResources().getIdentifier("baseline_notification_add_24", "drawable", "com.example.medicineorganizer");
            holder.notificationImage.setImageDrawable(ContextCompat.getDrawable(layoutInflater.getContext(),resourceId));
        } else if (notificationDto.getName().equals("Препарат заканчивается")) {
            int resourceId = layoutInflater.getContext().getResources().getIdentifier("baseline_crisis_alert_24", "drawable", "com.example.medicineorganizer");
            holder.notificationImage.setImageDrawable(ContextCompat.getDrawable(layoutInflater.getContext(),resourceId));
            notificationText = notificationDto.getComment();
        } else {
            notificationText = notificationDto.getName() + "\nПринять в количестве: " + notificationDto.getAmount()
                    + "\nВремя: " + notificationDto.getTime();
        }
        holder.notificationName.setText(notificationText);
    }

    @Override
    public int getItemCount() {
        return storage.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView notificationName;
        ImageView notificationImage;
        ViewHolder(View itemView) {
            super(itemView);
            notificationName = itemView.findViewById(R.id.viewPageNotificationName);
            notificationImage = itemView.findViewById(R.id.viewPageNotificationImage);
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
