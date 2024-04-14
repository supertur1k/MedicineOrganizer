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

import dto.FirstAidKit;

public class FirstAidKitsRecyclerViewAdapter extends RecyclerView.Adapter<FirstAidKitsRecyclerViewAdapter.ViewHolder> {

    public List<FirstAidKit> getStorage() {
        return storage;
    }

    public void setStorage(List<FirstAidKit> storage) {
        this.storage = storage;
    }

    private List<FirstAidKit> storage;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;

    public FirstAidKitsRecyclerViewAdapter(Context context, List<FirstAidKit> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.storage = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.first_aid_kit_in_view_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FirstAidKit firstAidKit = storage.get(position);
        holder.firstAidKitName.setText(firstAidKit.getName_of_the_first_aid_kit());
        holder.firstAidKitImage.setImageResource(R.drawable.first_aid_kit_image);

    }

    @Override
    public int getItemCount() {
        return storage.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView firstAidKitName;
        ImageView firstAidKitImage;

        ViewHolder(View itemView) {
            super(itemView);
            firstAidKitName = itemView.findViewById(R.id.firstAidKitName);
            firstAidKitImage = itemView.findViewById(R.id.firstAidKitImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    FirstAidKit getItem(int id) {
        return storage.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
