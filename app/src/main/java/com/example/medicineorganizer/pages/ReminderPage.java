package com.example.medicineorganizer.pages;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.actions.MedicineOrganizerServerService;
import com.example.medicineorganizer.data.FirstAidKitsDataHolder;
import com.example.medicineorganizer.data.SchedulesDataHolder;
import com.example.medicineorganizer.recyclerVies.SchedulesRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import dto.AppError;
import dto.ScheduleCreateRequestDTO;
import dto.ScheduleCreateResponseDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderPage extends AppCompatActivity implements SchedulesRecyclerViewAdapter.ItemClickListener{

    DrawerLayout drawerLayout;
    ImageView menu;
    LinearLayout mainPage, notifications, reminder, logout;
    BottomNavigationView bottomNavigationView;
    ImageButton reminderPageImageViewAdd;
    Dialog dialogCreateSchedule;
    Dialog dialogViewSchedule;
    Dialog progressDialog;
    Map<Integer, String> timePickerValues;
    // Хранилище данных
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    TextView noDataTextView;
    SchedulesRecyclerViewAdapter schedulesAdapter;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("medicine_organizer_client_user_storage", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_page);

        this.setMenuClickListeners();
        reminderPageImageViewAdd = (ImageButton) findViewById(R.id.reminderPageImageViewAdd);
        dialogCreateSchedule = new Dialog(ReminderPage.this);
        dialogViewSchedule = new Dialog(ReminderPage.this);

        reminderPageImageViewAdd.setOnClickListener(v -> {
            addSchedule();
        });

        noDataTextView = (TextView) findViewById(R.id.reminderPageNoSchedulesView);
        recyclerView = (RecyclerView) findViewById(R.id.reminderPageRecyclerViewSchedules);

        noDataTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        schedulesAdapter = new SchedulesRecyclerViewAdapter
                (this, SchedulesDataHolder.getInstance().getUserSchedules());

        showProgressDialog();

        schedulesAdapter.setClickListener(this);
        recyclerView.setAdapter(schedulesAdapter);

        fillRecyclerView();
    }


    private void fillRecyclerView() {
        MedicineOrganizerServerService.getSchedule(sharedPreferences.getString("username", "empty_username")
                , new Callback<Collection<ScheduleCreateResponseDTO>>() {
            @Override
            public void onResponse(Call<Collection<ScheduleCreateResponseDTO>> call, Response<Collection<ScheduleCreateResponseDTO>> response) {
                if (response.isSuccessful()) {
                    SchedulesDataHolder.getInstance().setUserSchedules((List<ScheduleCreateResponseDTO>) response.body());
                    noDataTextView.setVisibility(View.GONE);
                    schedulesAdapter.setStorage(response.body());
                    schedulesAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    hideProgressDialog();
                } else {
                    hideProgressDialog();
                    Log.e("schedule", response.message());
                }
            }

            @Override
            public void onFailure(Call<Collection<ScheduleCreateResponseDTO>> call, Throwable t) {
                hideProgressDialog();
                t.printStackTrace();
            }
        });

    }

    private void addSchedule() {
        timePickerValues = new HashMap<>();
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

        ImageButton increaseAmountButton = (ImageButton) dialogCreateSchedule.findViewById(R.id.increaseAmountButton);
        ImageButton decreaseAmountButton = (ImageButton) dialogCreateSchedule.findViewById(R.id.decreaseAmountButton);
        EditText integerAmount = (EditText) dialogCreateSchedule.findViewById(R.id.integerAmountValue);
        EditText stringAmount = (EditText) dialogCreateSchedule.findViewById(R.id.addMedicamentOtherAmountInput);

        increaseAmountButton.setOnClickListener(v -> {
            if (integerAmount.getText().length() > 0) {
                integerAmount.setText(String.valueOf(Integer.parseInt(String.valueOf(integerAmount.getText())) + 1));
            } else {
                integerAmount.setText("1");
            }
        });

        decreaseAmountButton.setOnClickListener(v -> {
            if (integerAmount.getText().length() > 0 && Integer.parseInt(String.valueOf(integerAmount.getText())) > 0) {
                integerAmount.setText(String.valueOf(Integer.parseInt(String.valueOf(integerAmount.getText())) - 1));
            } else {
                integerAmount.setText("0");
            }
        });


        CheckBox checkboxSpecifyOtherValue = (CheckBox) dialogCreateSchedule.findViewById(R.id.checkboxSpecifyOtherValue);
        checkboxSpecifyOtherValue.setOnClickListener(v->{
            if (checkboxSpecifyOtherValue.isChecked()) {
                increaseAmountButton.setVisibility(View.GONE);
                decreaseAmountButton.setVisibility(View.GONE);
                integerAmount.setVisibility(View.GONE);
                stringAmount.setVisibility(View.VISIBLE);
            } else {
                increaseAmountButton.setVisibility(View.VISIBLE);
                decreaseAmountButton.setVisibility(View.VISIBLE);
                integerAmount.setVisibility(View.VISIBLE);
                stringAmount.setVisibility(View.GONE);
            }
        });

        ImageButton increaseCourseDaysButton = (ImageButton) dialogCreateSchedule.findViewById(R.id.increaseCourseDaysButton);
        ImageButton decreaseCourseDaysButton = (ImageButton) dialogCreateSchedule.findViewById(R.id.decreaseCourseDaysButton);
        EditText scheduleDays = (EditText) dialogCreateSchedule.findViewById(R.id.scheduleDays);

        increaseCourseDaysButton.setOnClickListener(v -> {
            if (scheduleDays.getText().length() > 0) {
                scheduleDays.setText(String.valueOf(Integer.parseInt(String.valueOf(scheduleDays.getText())) + 1));
            } else {
                scheduleDays.setText("1");
            }
        });

        decreaseCourseDaysButton.setOnClickListener(v -> {
            if (scheduleDays.getText().length() > 0 && Integer.parseInt(String.valueOf(scheduleDays.getText())) > 0) {
                scheduleDays.setText(String.valueOf(Integer.parseInt(String.valueOf(scheduleDays.getText())) - 1));
            } else {
                scheduleDays.setText("0");
            }
        });

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

        addTimeFieldButton.setOnClickListener(v -> addTimeField(timeInputContainer));
        addTimeField(timeInputContainer);

        EditText commentEditText = (EditText) dialogCreateSchedule.findViewById(R.id.addScheduleComment);
        Button addScheduleCreateButton = (Button) dialogCreateSchedule.findViewById(R.id.addScheduleCreateButton);
        addScheduleCreateButton.setOnClickListener(v -> {
            ScheduleCreateRequestDTO scheduleDto = new ScheduleCreateRequestDTO();
            if (medicamentNameTextView.getText().length() < 1) {
                Toast.makeText(this, "Введите название препарата", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = String.valueOf(medicamentNameTextView.getText());
            scheduleDto.setName(name);
            String comment = null;
            if (commentEditText.getText().length() > 0) {
                comment = String.valueOf(commentEditText.getText());
                scheduleDto.setComment(comment);
            }
            String amount;
            if (integerAmount.getVisibility() == View.VISIBLE) {
                if (integerAmount.getText().length() < 1 || Integer.parseInt(String.valueOf(integerAmount.getText())) == 0) {
                    Toast.makeText(this, "Укажите дозировку", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    amount = String.valueOf(Integer.parseInt(String.valueOf(integerAmount.getText())));
                }
            } else {
                if (stringAmount.getText().length() < 1) {
                    Toast.makeText(this, "Укажите дозировку", Toast.LENGTH_SHORT).show();
                    return;
                }
                else amount = String.valueOf(stringAmount.getText());
            }
            scheduleDto.setAmount(amount);
            List<String> daysOfWeeks = new ArrayList<>();
            if (checkboxMonday.isChecked()) {
                daysOfWeeks.add("monday");
            }
            if (checkboxTuesday.isChecked()) {
                daysOfWeeks.add("tuesday");
            }
            if (checkboxWednesday.isChecked()) {
                daysOfWeeks.add("wednesday");
            }
            if (checkboxThursday.isChecked()) {
                daysOfWeeks.add("thursday");
            }
            if (checkboxFriday.isChecked()) {
                daysOfWeeks.add("friday");
            }
            if (checkboxSaturday.isChecked()) {
                daysOfWeeks.add("saturday");
            }
            if (checkboxSunday.isChecked()) {
                daysOfWeeks.add("sunday");
            }
            if (daysOfWeeks.size() == 0) {
                Toast.makeText(this, "Укажите дни недели", Toast.LENGTH_SHORT).show();
                return;
            }
            scheduleDto.setDaysOfWeeks(daysOfWeeks);
            if (scheduleDays.getText().length() < 1 || Integer.parseInt(String.valueOf(scheduleDays.getText())) == 0) {
                Toast.makeText(this, "Укажите продолжительность курса", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer duration = Integer.parseInt(String.valueOf(scheduleDays.getText()));
            scheduleDto.setDuration(duration);
            if (timePickerValues.entrySet().size() < 1) {
                Toast.makeText(this, "Укажите время приема препарата", Toast.LENGTH_SHORT).show();
                return;
            }
            for (Map.Entry<Integer, String> entry : timePickerValues.entrySet()) {
                String time = entry.getValue();
                if (time == null || time.isEmpty()) {
                    Toast.makeText(this, "Укажите время приема препарата", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            List<String> times = new ArrayList<>();
            for (Map.Entry<Integer, String> entry : timePickerValues.entrySet()) {
                Integer id = entry.getKey();
                String time = entry.getValue();
                times.add(time);
            }
            scheduleDto.setTimes(times);
            scheduleDto.setUsername(sharedPreferences.getString("username", "empty_username"));

            showProgressDialog();
            MedicineOrganizerServerService.createSchedule(scheduleDto, new Callback<Collection<ScheduleCreateResponseDTO>>() {
                @Override
                public void onResponse(Call<Collection<ScheduleCreateResponseDTO>> call, Response<Collection<ScheduleCreateResponseDTO>> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        SchedulesDataHolder.getInstance().setUserSchedules((List<ScheduleCreateResponseDTO>) response.body());
                        noDataTextView.setVisibility(View.GONE);
                        schedulesAdapter.setStorage(response.body());
                        schedulesAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
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
                public void onFailure(Call<Collection<ScheduleCreateResponseDTO>> call, Throwable t) {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    Log.e("schedules", t.getMessage());
                }
            });

            dialogCreateSchedule.cancel();
        });


        dialogCreateSchedule.show();
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

    private void addTimeField(LinearLayout timeInputContainer) {
        TimePicker timePicker = new TimePicker(new ContextThemeWrapper(this, android.R.style.Widget_Material_TimePicker));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 16);
        timePicker.setLayoutParams(layoutParams);

        int timePickerId = View.generateViewId();
        timePicker.setId(timePickerId);

        timePickerValues.put(timePickerId, "00:00");

        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            timePickerValues.put(view.getId(), formattedTime);
        });
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
        logout.setOnClickListener(v -> redirectActivity(ReminderPage.this, LoginPageActivity.class));

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

    @Override
    public void onItemClick(View view, int position) {
        dialogViewSchedule.setContentView(R.layout.check_schedule);
        dialogViewSchedule.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogViewSchedule.setCancelable(true);

        ImageButton viewScheduleCloseButton = (ImageButton) dialogViewSchedule.findViewById(R.id.viewScheduleCloseButton);
        viewScheduleCloseButton.setOnClickListener(v -> dialogViewSchedule.cancel());

        TextView nameOfTheSchedule = (TextView) dialogViewSchedule.findViewById(R.id.viewPageScheduleNameValue);
        nameOfTheSchedule.setText(schedulesAdapter.getStorage().get(position).getName());
        TextView scheduleDuration = (TextView) dialogViewSchedule.findViewById(R.id.viewPageScheduleDurationValue);
        scheduleDuration.setText(String.valueOf(schedulesAdapter.getStorage().get(position).getDuration()));

        List<String> daysList = schedulesAdapter.getStorage().get(position).getDaysOfWeeks().stream()
                .map(x -> x.replace("monday", "Пн"))
                .map(x -> x.replace("tuesday", "Вт"))
                .map(x -> x.replace("wednesday", "Ср"))
                .map(x -> x.replace("thursday", "Чт"))
                .map(x -> x.replace("friday", "Пт"))
                .map(x -> x.replace("saturday", "Сб"))
                .map(x -> x.replace("sunday", "Вс"))
                .collect(Collectors.toList());

        String days = String.join(", ", daysList);
        TextView daysOfWeeks = (TextView) dialogViewSchedule.findViewById(R.id.daysOfWeeksValue);
        daysOfWeeks.setText(days);

        TextView timesSchedule = (TextView) dialogViewSchedule.findViewById(R.id.timesScheduleValue);
        timesSchedule.setText(String.join(", ", schedulesAdapter.getStorage().get(position).getTimes()));

        if (schedulesAdapter.getStorage().get(position).getComment() != null && !schedulesAdapter.getStorage().get(position).getComment().isEmpty()) {
            ConstraintLayout scheduleCommentLayout = (ConstraintLayout) dialogViewSchedule.findViewById(R.id.scheduleCommentLayout);
            scheduleCommentLayout.setVisibility(View.VISIBLE);
            TextView scheduleComment = (TextView) dialogViewSchedule.findViewById(R.id.scheduleCommentValue);
            scheduleComment.setText(schedulesAdapter.getStorage().get(position).getComment());
        }

        Button viewPageDeleteScheduleButton = (Button) dialogViewSchedule.findViewById(R.id.viewPageDeleteScheduleButton);
        viewPageDeleteScheduleButton.setOnClickListener(v-> {
            showProgressDialog();
            MedicineOrganizerServerService.deleteSchedule(schedulesAdapter.getStorage().get(position).getIdOfSchedule(), new Callback<Collection<ScheduleCreateResponseDTO>>() {
                @Override
                public void onResponse(Call<Collection<ScheduleCreateResponseDTO>> call, Response<Collection<ScheduleCreateResponseDTO>> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        SchedulesDataHolder.getInstance().setUserSchedules((List<ScheduleCreateResponseDTO>) response.body());
                        schedulesAdapter.setStorage(response.body());
                        schedulesAdapter.notifyDataSetChanged();
                        dialogViewSchedule.cancel();
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
                public void onFailure(Call<Collection<ScheduleCreateResponseDTO>> call, Throwable t) {
                    hideProgressDialog();
                    t.printStackTrace();
                }
            });
        });


        dialogViewSchedule.show();

    }
}