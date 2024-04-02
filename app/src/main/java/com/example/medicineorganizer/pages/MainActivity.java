package com.example.medicineorganizer.pages;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.actions.MainPageActions;
import com.example.medicineorganizer.actions.MedicineOrganizerServerService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Collection;
import java.util.Optional;

import dto.FirstAidKit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FirstAidKitsRecyclerViewAdapter.ItemClickListener{
    DrawerLayout drawerLayout;
    ImageView menu;
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new FirstAidKitsRecyclerViewAdapter(this, MainPageActions.getArrayListOfFirstAidKitsNames());
        adapter.setClickListener(this);
        //adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


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
                                        adapter.getStorage().add(firstAidKit.getName_of_the_first_aid_kit());
                                        adapter.notifyDataSetChanged();
                                        dialog.cancel();
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
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}