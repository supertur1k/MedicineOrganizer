package com.example.medicineorganizer.pages;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.actions.MedicineOrganizerServerService;
import com.example.medicineorganizer.data.ActiveFirstAidKitDataHolder;
import com.example.medicineorganizer.data.FirstAidKitsDataHolder;
import com.example.medicineorganizer.recyclerVies.FirstAidKitsRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import dto.AppError;
import dto.FirstAidKit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FirstAidKitsRecyclerViewAdapter.ItemClickListener{
    DrawerLayout drawerLayout;
    ImageView menu;
    TextView textNoFak;
    EditText fakSearchInput;
    LinearLayout mainPage, notifications, reminder, logout;
    BottomNavigationView bottomNavigationView;
    Button addFirstAidKitButton;
    Dialog dialog;
    RecyclerView recyclerView;
    FirstAidKitsRecyclerViewAdapter adapter;

    // Хранилище данных
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("medicine_organizer_client_user_storage", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("username", null);

        this.setMenuClickListeners();

        addFirstAidKitButton = findViewById(R.id.mainPageAddFirstAidKit);
        dialog = new Dialog(MainActivity.this);

        recyclerView = findViewById(R.id.mainPageRecyclerViewFirstAidKits);
        textNoFak = findViewById(R.id.mainPageNoFAKData);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new FirstAidKitsRecyclerViewAdapter(this, FirstAidKitsDataHolder.getInstance().getFirstAidKits());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        fillFAKStorageWithValuesFromServer();

        fakSearchInput = findViewById(R.id.mainPageFilterEditText);

        addListenerOnButton();
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
        mainPage.setOnClickListener(v -> recreate());
        notifications.setOnClickListener(v -> redirectActivity(MainActivity.this, NotificationsPage.class));
        reminder.setOnClickListener(v -> redirectActivity(MainActivity.this, ReminderPage.class));
        logout.setOnClickListener(v -> redirectActivity(MainActivity.this, LoginPageActivity.class));

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
            switch (item.getItemId()) {
                case R.id.navBarMenuNotifications:
                    startActivity(new Intent(getApplicationContext(), NotificationsPage.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.navBarMenuMain:
                    return true;
                case R.id.navBarMenuReminder:
                    startActivity(new Intent(getApplicationContext(), ReminderPage.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        });
    }
    public void addListenerOnButton() {
        addFirstAidKitButton.setOnClickListener(
                v -> {
                    showCreateFAKDialogWindow();
                }
        );

        fakSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null || editable.toString().isEmpty() || editable.toString().length() < 1) {
                    adapter.setStorage(FirstAidKitsDataHolder.getInstance().getFirstAidKits());
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setStorage(FirstAidKitsDataHolder.getInstance().getFirstAidKitsFiltered(editable.toString()));
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void showCreateFAKDialogWindow() {
        dialog.setContentView(R.layout.create_first_aid_kit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        EditText name = (EditText) dialog.findViewById(R.id.addFakPageName);
        EditText desc = (EditText) dialog.findViewById(R.id.addFakPageDescription);
        Button addButton = (Button) dialog.findViewById(R.id.addFakPageCreateButton);

        addButton.setOnClickListener(
                v -> {
                    if (name.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Введите название аптечки", Toast.LENGTH_SHORT).show();
                    } else {
                        String nameFromEditText = name.getText().toString();
                        String descFromEditText = desc.getText().toString();
                        MedicineOrganizerServerService.createFirstAndKitForUser(username, nameFromEditText, descFromEditText, new Callback<Collection<FirstAidKit>>() {
                            @Override
                            public void onResponse(Call<Collection<FirstAidKit>> call, Response<Collection<FirstAidKit>> response) {
                                if (response.isSuccessful()) {
                                    Optional<Long> maxId = response.body().stream()
                                            .map(FirstAidKit::getId)
                                            .max(Long::compareTo);
                                    if (maxId.isPresent()) {
                                        FirstAidKit firstAidKit = new FirstAidKit(maxId.get(), nameFromEditText, descFromEditText, null);
                                        adapter.getStorage().add(firstAidKit);
                                        adapter.notifyDataSetChanged();
                                        dialog.cancel();
                                        textNoFak.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        fillFAKStorageWithValuesFromServer();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Collection<FirstAidKit>> call, Throwable t) {

                            }
                        });
                    }
                }
        );

        dialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        ActiveFirstAidKitDataHolder.getInstance().setFirstAidKit(adapter.getStorage().get(position));
        startActivity(new Intent(getApplicationContext(), FirstAitKitPage.class));
        String nameOfFak = adapter.getStorage().get(position).getName_of_the_first_aid_kit();
    }

    private void fillFAKStorageWithValuesFromServer() {
        MedicineOrganizerServerService.getFirstAndKitsByUsername(username, new Callback<List<FirstAidKit>>() {
            @Override
            public void onResponse(@NonNull Call<List<FirstAidKit>> call, @NonNull Response<List<FirstAidKit>> response) {
                if (response.isSuccessful()) {
                    FirstAidKitsDataHolder.getInstance().setFirstAidKits(response.body());
                    if (FirstAidKitsDataHolder.getInstance().getFirstAidKits().size() > 0) {
                        adapter.setStorage(FirstAidKitsDataHolder.getInstance().getFirstAidKits());
                        adapter.notifyDataSetChanged();
                        textNoFak.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        fakSearchInput.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (response.errorBody() != null) {
                        try {
                            Gson gson = new Gson();
                            AppError appError = gson.fromJson(response.errorBody().string(), AppError.class);
                            Log.e("error", "Не пришел ответ с аптечками с сервера");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FirstAidKit>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}