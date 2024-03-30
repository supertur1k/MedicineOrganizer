package com.example.medicineorganizer.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.medicineorganizer.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FirstAidKitsRecyclerViewAdapter.ItemClickListener{

    FirstAidKitsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAddMedicine = findViewById(R.id.buttonAddMedicine);


        // data to populate the RecyclerView with
        ArrayList<String> data = new ArrayList<>();
        data.add("Домашняя аптечка");


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFirstAidKits);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new FirstAidKitsRecyclerViewAdapter(this, data);
        adapter.setClickListener(this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}