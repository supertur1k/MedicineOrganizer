package com.example.medicineorganizer.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.data.FirstAidKitsDataHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ReminderPage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout mainPage, notifications, reminder, logout;
    BottomNavigationView bottomNavigationView;
    ImageButton reminderPageImageViewAdd;
    Dialog dialogCreateSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_page);

        this.setMenuClickListeners();
        reminderPageImageViewAdd = (ImageButton) findViewById(R.id.reminderPageImageViewAdd);
        dialogCreateSchedule = new Dialog(ReminderPage.this);

        reminderPageImageViewAdd.setOnClickListener(v -> {
            addSchedule();
        });
    }

    private void addSchedule() {
        dialogCreateSchedule.setContentView(R.layout.create_schedule);
        dialogCreateSchedule.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCreateSchedule.setCancelable(true);

        List<String> medicamentNames = new ArrayList<>();

        FirstAidKitsDataHolder.getInstance().getFirstAidKits().stream().forEach(x -> {
            if (x.getMedicaments() != null) {
                x.getMedicaments().forEach(y -> {
                    if (y != null) {
                        medicamentNames.add(y.getName());
                    }
                });
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, medicamentNames);
        AutoCompleteTextView medicamentNameTextView = dialogCreateSchedule.findViewById(R.id.addScheduleMedicamentName);
        medicamentNameTextView.setAdapter(adapter);

        CheckBox checkboxMonday = (CheckBox) dialogCreateSchedule.findViewById(R.id.checkboxMonday);
        CheckBox checkboxTuesday = (CheckBox) dialogCreateSchedule.findViewById(R.id.checkboxTuesday);
        CheckBox checkboxWednesday = (CheckBox) dialogCreateSchedule.findViewById(R.id.checkboxWednesday);
        CheckBox checkboxThursday = (CheckBox) dialogCreateSchedule.findViewById(R.id.checkboxThursday);
        CheckBox checkboxFriday = (CheckBox) dialogCreateSchedule.findViewById(R.id.checkboxFriday);
        CheckBox checkboxSaturday = (CheckBox) dialogCreateSchedule.findViewById(R.id.checkboxSaturday);
        CheckBox checkboxSunday = (CheckBox) dialogCreateSchedule.findViewById(R.id.checkboxSunday);

        CheckBox checkboxEveryday = (CheckBox) dialogCreateSchedule.findViewById(R.id.checkboxEveryday);

        ImageButton closeDialog = (ImageButton) dialogCreateSchedule.findViewById(R.id.addScheduleCloseButton);
        closeDialog.setOnClickListener(v -> {
            dialogCreateSchedule.cancel();
        });
        checkboxEveryday.setOnClickListener(v -> {
            if (checkboxEveryday.isChecked()) {
                checkboxMonday.setChecked(true);
                checkboxTuesday.setChecked(true);
                checkboxWednesday.setChecked(true);
                checkboxThursday.setChecked(true);
                checkboxFriday.setChecked(true);
                checkboxSaturday.setChecked(true);
                checkboxSunday.setChecked(true);
            } else {
                checkboxMonday.setChecked(false);
                checkboxTuesday.setChecked(false);
                checkboxWednesday.setChecked(false);
                checkboxThursday.setChecked(false);
                checkboxFriday.setChecked(false);
                checkboxSaturday.setChecked(false);
                checkboxSunday.setChecked(false);
            }
        });


        LinearLayout timeInputContainer = dialogCreateSchedule.findViewById(R.id.addScheduleTimeContainer);
        Button addTimeFieldButton = dialogCreateSchedule.findViewById(R.id.addScheduleAddTimeButton);

        addTimeFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTimeField(timeInputContainer);
            }
        });
        addTimeField(timeInputContainer);
        dialogCreateSchedule.show();
    }

    private void addTimeField(LinearLayout timeInputContainer) {
        TimePicker timePicker = new TimePicker(new ContextThemeWrapper(this, android.R.style.Widget_Material_TimePicker));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 16);
        timePicker.setLayoutParams(layoutParams);

        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> Log.d("TimePicker", "Время изменено: " + hourOfDay + ":" + minute));
        timeInputContainer.addView(timePicker);
    }
    private void setMenuClickListeners() {
        drawerLayout = findViewById(R.id.drawerLayout);
        menu = findViewById(R.id.menu);
        notifications = findViewById(R.id.notifications);
        mainPage = findViewById(R.id.mainPage);
        reminder = findViewById(R.id.medicationReminder);
        logout = findViewById(R.id.logout);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.navBarMenuReminder);
        this.setBottomMenuListener();
        menu.setOnClickListener(v -> openDrawer(drawerLayout));
        reminder.setOnClickListener(v -> recreate());
        notifications.setOnClickListener(v -> redirectActivity(ReminderPage.this, NotificationsPage.class));
        mainPage.setOnClickListener(v -> redirectActivity(ReminderPage.this, MainActivity.class));
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
                    return true;
            }
            return false;
        });
    }
}