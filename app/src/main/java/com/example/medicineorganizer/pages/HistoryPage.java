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
import android.util.Log;
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
import com.example.medicineorganizer.data.FirstAidKitsDataHolder;
import com.example.medicineorganizer.data.HistoryDataHolder;
import com.example.medicineorganizer.recyclerVies.HistoryRecyclerViewAdapter;
import com.example.medicineorganizer.recyclerVies.NotificationsRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dto.AppError;
import dto.ListFaksDto;
import dto.NotificationDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryPage extends AppCompatActivity implements HistoryRecyclerViewAdapter.ItemClickListener{

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout mainPage, notifications, reminder, logout, history;
    BottomNavigationView bottomNavigationView;
    TextView historyNoDataMessage;

    Dialog progressDialog;
    RecyclerView recyclerViewHistory;
    HistoryRecyclerViewAdapter adapter;
    SharedPreferences sharedPreferences;
    Dialog historyDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("medicine_organizer_client_user_storage", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setMenuClickListeners();
        getSchedulesForUserFak();

        historyNoDataMessage = (TextView) findViewById(R.id.historyNoDataMessage);
        recyclerViewHistory = (RecyclerView) findViewById(R.id.historyRecyclerView);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryRecyclerViewAdapter(this, new ArrayList<>());
        adapter.setClickListener(this);
        recyclerViewHistory.setAdapter(adapter);

        historyDialog = new Dialog(HistoryPage.this);

    }

    private void fillStorage() {
        List<NotificationDto> notifications = HistoryDataHolder.getInstance().getNotifications();
        adapter.setStorage(notifications);
        adapter.notifyDataSetChanged();
        if (notifications.size() > 0) {
            historyNoDataMessage.setVisibility(View.GONE);
            recyclerViewHistory.setVisibility(View.VISIBLE);
        } else {
            historyNoDataMessage.setVisibility(View.VISIBLE);
            recyclerViewHistory.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        historyDialog.setContentView(R.layout.history_dialog);
        historyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        historyDialog.setCancelable(true);


        ImageButton historyDialogCloseButton = historyDialog.findViewById(R.id.historyDialogCloseButton);
        historyDialogCloseButton.setOnClickListener(v -> {
            historyDialog.cancel();
        });

        NotificationDto notificationDto = adapter.getStorage().get(position);;

        TextView dialogHistoryText = historyDialog.findViewById(R.id.dialogHistoryText);
        dialogHistoryText.setText(String.format("Препарат: %s,\nПринял пользователь: %s,\nВ количестве: %s\nВремя приема: %s %s\n",
                notificationDto.getName(), notificationDto.getUsername(), notificationDto.getAmount(), notificationDto.getDayOfTheWeek(), notificationDto.getTime()));
        historyDialog.show();
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
        notifications.setOnClickListener(v -> redirectActivity(HistoryPage.this, NotificationsPage.class));
        mainPage.setOnClickListener(v -> redirectActivity(HistoryPage.this, MainActivity.class));
        reminder.setOnClickListener(v -> redirectActivity(HistoryPage.this, ReminderPage.class));
        logout.setOnClickListener(v -> redirectActivity(HistoryPage.this, LoginPageActivity.class));
        history.setOnClickListener(v -> recreate());
    }

    private void getSchedulesForUserFak() {
        List<Long> ids = new ArrayList<>();
        FirstAidKitsDataHolder.getInstance().getFirstAidKits().forEach(x -> ids.add(x.getId()));

        showProgressDialog();
        MedicineOrganizerServerService.getNotificationsOfAllUsersOfFaks(ids, new Callback<Collection<NotificationDto>>() {
            @Override
            public void onResponse(Call<Collection<NotificationDto>> call, Response<Collection<NotificationDto>> response) {
                hideProgressDialog();
                if (response.isSuccessful()) {
                    HistoryDataHolder.getInstance().setNotifications(response.body());
                    fillStorage();
                } else {
                    if (response.errorBody() != null) {
                        try {
                            Gson gson = new Gson();
                            AppError appError = gson.fromJson(response.errorBody().string(), AppError.class);
                            Toast.makeText(getApplicationContext(), appError.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error", appError.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("error", response.message());
                        Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Collection<NotificationDto>> call, Throwable t) {
                hideProgressDialog();
                Log.e("error", t.toString());
                Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
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
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
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