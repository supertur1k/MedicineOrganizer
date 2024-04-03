package com.example.medicineorganizer.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.actions.MedicineOrganizerServerService;
import com.example.medicineorganizer.data.ActiveFirstAidKitDataHolder;
import com.example.medicineorganizer.data.FirstAidKitsDataHolder;
import com.example.medicineorganizer.recyclerVies.FirstAidKitsRecyclerViewAdapter;
import com.example.medicineorganizer.recyclerVies.MedicinesRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import dto.FirstAidKit;
import dto.Medicament;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstAitKitPage extends AppCompatActivity implements MedicinesRecyclerViewAdapter.ItemClickListener {

    TextView nameOfFak;
    TextView descOfFak;
    Button addMedicine;
    RecyclerView recyclerView;
    MedicinesRecyclerViewAdapter adapter;
    TextView mainPageNoMedicinesData;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout mainPage, notifications, reminder, logout;
    BottomNavigationView bottomNavigationView;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_ait_kit_page);
        this.setMenuClickListeners();

        nameOfFak = findViewById(R.id.fakPageNameTextView);
        descOfFak = findViewById(R.id.fakPageDescTextView);
        this.setFakData();

        addMedicine = findViewById(R.id.fakPageAddMedicine);
        dialog = new Dialog(FirstAitKitPage.this);

        mainPageNoMedicinesData = findViewById(R.id.fakPageNoMedicinesData);
        recyclerView = findViewById(R.id.fakPageRecyclerViewMedicines);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MedicinesRecyclerViewAdapter(this, (List) ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicaments());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        if (ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicaments().size() > 0) {
            mainPageNoMedicinesData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        FirstAidKit firstAitKit = ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit();
        addListenerOnButton();
    }

    @Override
    public void onItemClick(View view, int position) {
        //ActiveFirstAidKitDataHolder.getInstance().setFirstAidKit(adapter.getStorage().get(position).);
        //startActivity(new Intent(getApplicationContext(), FirstAitKitPage.class));
        Toast.makeText(FirstAitKitPage.this, "Вы нажали на " + adapter.getStorage().get(position).getName(), Toast.LENGTH_SHORT).show();

        String nameOfMedicine = adapter.getStorage().get(position).getName();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private void addListenerOnButton() {
        addMedicine.setOnClickListener(
                v -> {
                    showCreateFAKDialogWindow();
                }
        );
    }

    private void showCreateFAKDialogWindow() {
        dialog.setContentView(R.layout.create_medicament);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        EditText name = (EditText) dialog.findViewById(R.id.addMedicamentPageName);
        EditText desc = (EditText) dialog.findViewById(R.id.addMedicamentPageDescription);
        Button addButton = (Button) dialog.findViewById(R.id.addMedicamentPageCreateButton);

        addButton.setOnClickListener(
                v -> {
                    if (name.getText().toString().isEmpty()) {
                        Toast.makeText(FirstAitKitPage.this, "Введите название препарата", Toast.LENGTH_SHORT).show();
                    } else {
                        String nameFromEditText = name.getText().toString();
                        String descFromEditText = desc.getText().toString();
//                        MedicineOrganizerServerService.createFirstAndKitForUser(username, nameFromEditText, descFromEditText, new Callback<Collection<FirstAidKit>>() {
//                            @Override
//                            public void onResponse(Call<Collection<FirstAidKit>> call, Response<Collection<FirstAidKit>> response) {
//                                if (response.isSuccessful()) {
//                                    Optional<Long> maxId = response.body().stream()
//                                            .map(FirstAidKit::getId)
//                                            .max(Long::compareTo);
//                                    if (maxId.isPresent()) {
//                                        FirstAidKit firstAidKit = new FirstAidKit(maxId.get(), nameFromEditText, descFromEditText, null);
//                                        adapter.getStorage().add(firstAidKit);
//                                        adapter.notifyDataSetChanged();
//                                        dialog.cancel();
//                                        textNoFak.setVisibility(View.GONE);
//                                        recyclerView.setVisibility(View.VISIBLE);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<Collection<FirstAidKit>> call, Throwable t) {
//
//                            }
//                        });
                    }
                }
        );

        dialog.show();
    }
    private void setFakData() {
        FirstAidKit firstAidKit = ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit();
        if (firstAidKit.getName_of_the_first_aid_kit() != null &&
                !firstAidKit.getName_of_the_first_aid_kit().isEmpty()) {
            nameOfFak.setText("Аптечка: " + firstAidKit.getName_of_the_first_aid_kit());
        }
        if (firstAidKit.getDescription() != null &&
                !firstAidKit.getDescription().isEmpty()) {
            descOfFak.setText(firstAidKit.getDescription());
            descOfFak.setVisibility(View.VISIBLE);
        }
    }

    private void setMenuClickListeners() {
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        notifications = findViewById(R.id.notifications);
        mainPage = findViewById(R.id.mainPage);
        reminder = findViewById(R.id.medicationReminder);
        logout = findViewById(R.id.logout);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.navBarMenuMain);
        this.setBottomMenuListener();
        menu.setOnClickListener(v -> openDrawer(drawerLayout));
        notifications.setOnClickListener(v -> redirectActivity(FirstAitKitPage.this, NotificationsPage.class));
        mainPage.setOnClickListener(v -> redirectActivity(FirstAitKitPage.this, MainActivity.class));
        reminder.setOnClickListener(v -> redirectActivity(FirstAitKitPage.this, ReminderPage.class));
    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }

    private void setBottomMenuListener() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navBarMenuNotifications:
                    startActivity(new Intent(getApplicationContext(), NotificationsPage.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.navBarMenuMain:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.navBarMenuReminder:
                    startActivity(new Intent(getApplicationContext(), ReminderPage.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        });
    }
}