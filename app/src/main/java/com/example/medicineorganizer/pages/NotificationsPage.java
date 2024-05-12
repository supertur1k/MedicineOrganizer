package com.example.medicineorganizer.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.actions.MedicineOrganizerServerService;
import com.example.medicineorganizer.data.ActiveFirstAidKitDataHolder;
import com.example.medicineorganizer.recyclerVies.MedicinesRecyclerViewAdapter;
import com.example.medicineorganizer.recyclerVies.NotificationsRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dto.AppError;
import dto.NotificationDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsPage extends AppCompatActivity implements NotificationsRecyclerViewAdapter.ItemClickListener {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout mainPage, notifications, reminder, logout, history;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerViewNotifications;
    TextView notificationsNoData;
    NotificationsRecyclerViewAdapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Dialog progressDialog;
    Dialog notificationDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("medicine_organizer_client_user_storage", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_page);
        this.setMenuClickListeners();

        notificationsNoData = (TextView) findViewById(R.id.notificationPageNoDataMessage);
        recyclerViewNotifications = (RecyclerView) findViewById(R.id.notificationPageRecView);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationsRecyclerViewAdapter(this, new ArrayList<>());
        adapter.setClickListener(this);
        recyclerViewNotifications.setAdapter(adapter);

        notificationDialog = new Dialog(NotificationsPage.this);

        fillStorageOfNotifications();
    }

    private void fillStorageOfNotifications() {
        showProgressDialog();
        MedicineOrganizerServerService.getNotifications(sharedPreferences.getString("username", "empty_username"), new Callback<Collection<NotificationDto>>() {
            @Override
            public void onResponse(Call<Collection<NotificationDto>> call, Response<Collection<NotificationDto>> response) {
                hideProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        notificationsNoData.setVisibility(View.GONE);
                        recyclerViewNotifications.setVisibility(View.VISIBLE);
                    }
                    adapter.setStorage(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    if (response.errorBody() != null) {
                        try {
                            Gson gson = new Gson();
                            AppError appError = gson.fromJson(response.errorBody().string(), AppError.class);
                            Toast.makeText(getApplicationContext(), appError.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Collection<NotificationDto>> call, Throwable t) {
                t.printStackTrace();
                hideProgressDialog();
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        notificationDialog.setContentView(R.layout.notification_dialog);
        notificationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notificationDialog.setCancelable(true);

        ConstraintLayout acceptAndDeclineButtonsContainer = notificationDialog.findViewById(R.id.acceptAndDeclineButtonsContainer);
        Button acceptButton = notificationDialog.findViewById(R.id.acceptButton);
        Button declineButton = notificationDialog.findViewById(R.id.declineButton);
        Button readNotification = notificationDialog.findViewById(R.id.readNotification);
        TextView alreadyRead = notificationDialog.findViewById(R.id.alreadyRead);

        acceptButton.setOnClickListener(v -> {
            MedicineOrganizerServerService.addAnExistingFirstAidKitToUser(
                    sharedPreferences.getString("username", "empty_username"), adapter.getStorage().get(position).getIdOfTheSchedule(), new Callback<Collection<NotificationDto>>() {
                @Override
                public void onResponse(Call<Collection<NotificationDto>> call, Response<Collection<NotificationDto>> response) {
                    if (response.isSuccessful()) {
                        deleteNotificationFromUser(position);
                        notificationDialog.cancel();
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                Gson gson = new Gson();
                                AppError appError = gson.fromJson(response.errorBody().string(), AppError.class);
                                Toast.makeText(getApplicationContext(), appError.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Collection<NotificationDto>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                }
            });
        });
        declineButton.setOnClickListener(v -> {
            deleteNotificationFromUser(position);
        });

        readNotification.setOnClickListener(v -> {
            showProgressDialog();
            MedicineOrganizerServerService.readNotification(adapter.getStorage().get(position).getIdOfNotification(), sharedPreferences.getString("username", "empty_username"), new Callback<Collection<NotificationDto>>() {
                @Override
                public void onResponse(Call<Collection<NotificationDto>> call, Response<Collection<NotificationDto>> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        alreadyRead.setVisibility(View.VISIBLE);
                        readNotification.setVisibility(View.GONE);
                        fillStorageOfNotifications();
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                Gson gson = new Gson();
                                AppError appError = gson.fromJson(response.errorBody().string(), AppError.class);
                                Toast.makeText(getApplicationContext(), appError.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Collection<NotificationDto>> call, Throwable t) {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                }
            });
        });
        TextView notificationText = notificationDialog.findViewById(R.id.notificationText);

        String notificationTextValue = "Приглашение в аптечку";
        if (adapter.getStorage().get(position).getName().equals("Приглашение в аптечку")) {
            acceptAndDeclineButtonsContainer.setVisibility(View.VISIBLE);
            readNotification.setVisibility(View.GONE);
            notificationTextValue = String.format("Пользователь %s приглашает Вас в аптечку", adapter.getStorage().get(position).getComment().replace("system_user_invite_from", ""));
        } else {
            acceptAndDeclineButtonsContainer.setVisibility(View.GONE);
            readNotification.setVisibility(View.VISIBLE);
            notificationTextValue = adapter.getStorage().get(position).getName() + "\nПринять в количестве: " + adapter.getStorage().get(position).getAmount()
                    + "\nВремя: " + adapter.getStorage().get(position).getTime() + "\n";
            if (adapter.getStorage().get(position).getReceived()) {
                alreadyRead.setVisibility(View.VISIBLE);
                readNotification.setVisibility(View.GONE);
            }
        }
        notificationText.setText(notificationTextValue);


        ImageButton notificationCloseButton = notificationDialog.findViewById(R.id.notificationCloseButton);
        notificationCloseButton.setOnClickListener(v -> {
            notificationDialog.cancel();
        });

        notificationDialog.show();
    }

    private void deleteNotificationFromUser(int position) {
        showProgressDialog();
        MedicineOrganizerServerService.deleteNotification(adapter.getStorage().get(position).getIdOfNotification(), new Callback<Collection<NotificationDto>>() {
            @Override
            public void onResponse(Call<Collection<NotificationDto>> call, Response<Collection<NotificationDto>> response) {
                hideProgressDialog();
                if (response.isSuccessful()) {
                    fillStorageOfNotifications();
                    notificationDialog.cancel();
                } else {
                    if (response.errorBody() != null) {
                        try {
                            Gson gson = new Gson();
                            AppError appError = gson.fromJson(response.errorBody().string(), AppError.class);
                            Toast.makeText(getApplicationContext(), appError.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Collection<NotificationDto>> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    private void setMenuClickListeners() {
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        notifications = findViewById(R.id.notifications);
        mainPage = findViewById(R.id.mainPage);
        reminder = findViewById(R.id.medicationReminder);
        logout = findViewById(R.id.logout);
        history = findViewById(R.id.history);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.navBarMenuNotifications);
        this.setBottomMenuListener();
        menu.setOnClickListener(v -> openDrawer(drawerLayout));
        notifications.setOnClickListener(v -> recreate());
        mainPage.setOnClickListener(v -> redirectActivity(NotificationsPage.this, MainActivity.class));
        reminder.setOnClickListener(v -> redirectActivity(NotificationsPage.this, ReminderPage.class));
        logout.setOnClickListener(v -> redirectActivity(NotificationsPage.this, LoginPageActivity.class));
        history.setOnClickListener(v -> redirectActivity(NotificationsPage.this, HistoryPage.class));
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