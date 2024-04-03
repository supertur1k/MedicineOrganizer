package com.example.medicineorganizer.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.data.ActiveFirstAidKitDataHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dto.FirstAidKit;

public class FirstAitKitPage extends AppCompatActivity {

    TextView nameOfFak;
    TextView descOfFak;
    Button addMedicine;
    TextView mainPageNoMedicinesData;
    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout mainPage, notifications, reminder, logout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_ait_kit_page);
        this.setMenuClickListeners();

        addMedicine = findViewById(R.id.fakPageAddMedicine);
        nameOfFak = findViewById(R.id.fakPageNameTextView);
        descOfFak = findViewById(R.id.fakPageDescTextView);
        this.setFakData();


        mainPageNoMedicinesData = findViewById(R.id.fakPageNoMedicinesData);

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