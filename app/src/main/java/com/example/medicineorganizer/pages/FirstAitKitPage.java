package com.example.medicineorganizer.pages;

import static java.security.AccessController.getContext;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.Retrofit.RetrofitMedicineOrganizerServerService;
import com.example.medicineorganizer.actions.MedicineOrganizerServerService;
import com.example.medicineorganizer.data.ActiveFirstAidKitDataHolder;
import com.example.medicineorganizer.data.FirstAidKitsDataHolder;
import com.example.medicineorganizer.data.SchedulesDataHolder;
import com.example.medicineorganizer.recyclerVies.MedicinesRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import dto.AppError;
import dto.FirstAidKit;
import dto.FirstAidKitIdUsernameDTO;
import dto.Medicament;
import dto.NotificationDto;
import dto.ScheduleCreateResponseDTO;
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
    ImageButton settingsButton;
    LinearLayout mainPage, notifications, reminder, logout;
    BottomNavigationView bottomNavigationView;
    Dialog dialog;
    Dialog dialogMedicament;

    Dialog settingsDialog;

    Dialog progressDialog;
    FirstAidKit firstAitKitFromStorage;

    ConstraintLayout fakPageFakFilter;
    EditText fakPageFilterName;

    Spinner spinner;
    String[] formsArray;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("medicine_organizer_client_user_storage", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_ait_kit_page);
        this.setMenuClickListeners();

        fakPageFakFilter = findViewById(R.id.fakPageFakFilter);
        nameOfFak = findViewById(R.id.fakPageNameTextView);
        descOfFak = findViewById(R.id.fakPageDescTextView);
        this.setFakData();

        settingsButton = (ImageButton) findViewById(R.id.fakPageSettingsButton);
        fakPageFilterName = findViewById(R.id.fakPageFilterName);
        addMedicine = findViewById(R.id.fakPageAddMedicine);
        dialog = new Dialog(FirstAitKitPage.this);
        settingsDialog = new Dialog(FirstAitKitPage.this);
        dialogMedicament = new Dialog(FirstAitKitPage.this);

        mainPageNoMedicinesData = findViewById(R.id.fakPageNoMedicinesData);
        recyclerView = findViewById(R.id.fakPageRecyclerViewMedicines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicinesRecyclerViewAdapter(this, (List) ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicaments());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        setVisibilityIfMedicamentDataSetIsNotEmpty();

        firstAitKitFromStorage = ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit();
        addListenerOnButton();
        addSettingsButtonListener();

        spinner = findViewById(R.id.fakPageFilterForm);
        formsArray = new String[]{"Все", "Таблетки", "Капсулы", "Сиропы", "Мази", "Кремы", "Растворы"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, formsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        addSpinnerListener();
    }


    private void addSettingsButtonListener() {
        settingsButton.setOnClickListener(v -> {
            showSettingsDialog();
        });
    }

    private void showSettingsDialog() {
        settingsDialog.setContentView(R.layout.settings_dialog);
        settingsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        settingsDialog.setCancelable(true);

        TextView header = settingsDialog.findViewById(R.id.viewPageSettingsHeader);
        String newHeaderValue = header.getText() + ": " + ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getName_of_the_first_aid_kit();
        header.setText(newHeaderValue);
        String amount = String.valueOf(ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicaments().size());
        ImageButton close = settingsDialog.findViewById(R.id.settingsCloseButton);
        close.setOnClickListener(v -> {settingsDialog.cancel();});

        TextView viewPageSettingsAmountValue = settingsDialog.findViewById(R.id.viewPageSettingsAmountValue);
        viewPageSettingsAmountValue.setText(amount);
        if (ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getDescription() != null &&
                !ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getDescription().isEmpty()) {
            ConstraintLayout settingsDescription = settingsDialog.findViewById(R.id.viewPageSettingsCLayout);
            settingsDescription.setVisibility(View.VISIBLE);
            TextView settingPageDescValue = settingsDialog.findViewById(R.id.settingPageDescValue);
            settingPageDescValue.setText(ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getDescription());
        }
        Button deleteFAKButton = settingsDialog.findViewById(R.id.deleteFAKButton);
        deleteFAKButton.setOnClickListener(v -> {
            MedicineOrganizerServerService.removeFirstAndFromForUser(
                    sharedPreferences.getString("username", null), ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getId(),
                    new Callback<Collection<FirstAidKit>>() {
                        @Override
                        public void onResponse(Call<Collection<FirstAidKit>> call, Response<Collection<FirstAidKit>> response) {
                            settingsDialog.cancel();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Collection<FirstAidKit>> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        ImageButton addUser = (ImageButton) settingsDialog.findViewById(R.id.reminderPageImageViewAdd);
        Button inviteUserToFakButton = (Button) settingsDialog.findViewById(R.id.inviteUserToFakButton);
        TextView inviteSend = (TextView) settingsDialog.findViewById(R.id.inviteSend);
        EditText usernameInput = (EditText) settingsDialog.findViewById(R.id.usernameInput);

        addUser.setOnClickListener(v -> {
            inviteSend.setVisibility(View.GONE);
            inviteUserToFakButton.setVisibility(View.VISIBLE);
            usernameInput.setText("");
            usernameInput.setVisibility(View.VISIBLE);
        });

        inviteUserToFakButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString();
            if (username.isEmpty()) {
                Toast.makeText(this, "Введите имя пользователя", Toast.LENGTH_SHORT).show();
            } else {
                MedicineOrganizerServerService.notificationForAddingExistingFirstAidKitToUser(username, ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getId(), new Callback<Collection<NotificationDto>>() {
                    @Override
                    public void onResponse(Call<Collection<NotificationDto>> call, Response<Collection<NotificationDto>> response) {
                        if (response.isSuccessful()) {
                            inviteSend.setText(String.format("Приглашение пользователю %s успешно отправлено", username));
                            inviteSend.setVisibility(View.VISIBLE);
                            inviteSend.setTextColor(getResources().getColor(R.color.saladGreen, getApplicationContext().getTheme()));
                            inviteUserToFakButton.setVisibility(View.GONE);
                            usernameInput.setVisibility(View.GONE);
                        } else {
                            inviteSend.setTextColor(getResources().getColor(R.color.redButton, getApplicationContext().getTheme()));
                            if (response.errorBody() != null) {
                                try {
                                    Gson gson = new Gson();
                                    AppError appError = gson.fromJson(response.errorBody().string(), AppError.class);
                                    if (appError.getMessage().equals("Пользователь не найден")) {
                                        inviteSend.setText(String.format("Пользователь %s не найден", username));
                                        inviteSend.setVisibility(View.VISIBLE);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                inviteSend.setTextColor(getResources().getColor(R.color.redButton, getApplicationContext().getTheme()));
                                Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Collection<NotificationDto>> call, Throwable t) {
                        inviteSend.setTextColor(getResources().getColor(R.color.redButton, getApplicationContext().getTheme()));
                        Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        settingsDialog.show();
    }
    private void addSpinnerListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = formsArray[position];
                if (selectedItem.equals("Все")) {
                    adapter.setStorage(ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicaments());
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setStorage(ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicamentsFilteredByReleaseForm(selectedItem));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

    }

    private void setVisibilityIfMedicamentDataSetIsNotEmpty() {
        if (ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicaments().size() > 0) {
            fakPageFakFilter.setVisibility(View.VISIBLE);
            mainPageNoMedicinesData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onItemClick(View view, int position) {
        showMedicamentDialog(adapter.getStorage().get(position));
    }

    private void showMedicamentDialog(Medicament medicament) {
        dialogMedicament.setContentView(R.layout.check_medicament);
        dialogMedicament.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogMedicament.setCancelable(true);
        ImageButton close = dialogMedicament.findViewById(R.id.showMedicamentCloseButton);
        close.setOnClickListener(v -> {dialogMedicament.cancel();});

        TextView name = (TextView) dialogMedicament.findViewById(R.id.viewPageMedicamentNameValue);
        TextView releaseForm = (TextView) dialogMedicament.findViewById(R.id.viewPageMedicamentReleaseForm);
        TextView releaseFormValue = (TextView) dialogMedicament.findViewById(R.id.viewPageMedicamentReleaseFormValue);
        TextView amount = (TextView) dialogMedicament.findViewById(R.id.viewPageMedicamentAmount);
        TextView amountValue = (TextView) dialogMedicament.findViewById(R.id.viewPageAmountValue);
        TextView directionsValue = (TextView) dialogMedicament.findViewById(R.id.viewPageDirectionsForUseValue);
        ConstraintLayout viewPageDirectionsForUseLayout = (ConstraintLayout) dialogMedicament.findViewById(R.id.viewPageDirectionsForUseLayout);

        TextView indicationsValue = (TextView) dialogMedicament.findViewById(R.id.viewPageIndicationsValue);
        ConstraintLayout indicationsValueConstraintLayout = (ConstraintLayout) dialogMedicament.findViewById(R.id.viewPageIndicationsForUseLayout);

        TextView contraindicationsValue = (TextView) dialogMedicament.findViewById(R.id.viewPageContraIndicationsValue);
        ConstraintLayout contraindicationsForUseLayout = (ConstraintLayout) dialogMedicament.findViewById(R.id.viewPageContraindicationsForUseLayout);

        Button deleteMedicamentButton = (Button) dialogMedicament.findViewById(R.id.viewPageDeleteMedicamentButton);

        name.setText(medicament.getName());

        if (medicament.getReleaseForm() != null && !medicament.getReleaseForm().isEmpty()) {
            releaseForm.setVisibility(View.VISIBLE);
            releaseFormValue.setText(medicament.getReleaseForm());
            releaseFormValue.setVisibility(View.VISIBLE);
        }

        if (medicament.getAmount() != null && !medicament.getAmount().isEmpty()) {
            amount.setVisibility(View.VISIBLE);
            amountValue.setText(medicament.getAmount());
            amountValue.setVisibility(View.VISIBLE);
        }

        if (medicament.getDirectionsForUse() != null && !medicament.getDirectionsForUse().isEmpty()) {
            directionsValue.setText(medicament.getDirectionsForUse());
            viewPageDirectionsForUseLayout.setVisibility(View.VISIBLE);
        }

        if (medicament.getIndicationsForUse() != null && !medicament.getIndicationsForUse().isEmpty()) {
            indicationsValue.setText(medicament.getIndicationsForUse());
            indicationsValueConstraintLayout.setVisibility(View.VISIBLE);
        }

        if (medicament.getContraindications() != null && !medicament.getContraindications().isEmpty()) {
            contraindicationsValue.setText(medicament.getContraindications());
            contraindicationsForUseLayout.setVisibility(View.VISIBLE);
        }

        deleteMedicamentButton.setOnClickListener(v -> {
            MedicineOrganizerServerService.deleteMedicamentForUser(firstAitKitFromStorage.getId(), medicament.getName(), new Callback<Collection<Medicament>>() {
                @Override
                public void onResponse(Call<Collection<Medicament>> call, Response<Collection<Medicament>> response) {
                    if (response.isSuccessful()) {
                        Collection<Medicament> responseBody = response.body();
                        adapter.setStorage(responseBody);
                        adapter.notifyDataSetChanged();
                        ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().setMedicaments(responseBody);
                        if (responseBody == null || responseBody.size() < 1) {
                            fakPageFakFilter.setVisibility(View.GONE);
                            mainPageNoMedicinesData.setVisibility(View.VISIBLE);
                        }
                        dialogMedicament.cancel();
                    } else {
                        Log.e("not successful deleting", response.message());
                    }
                }

                @Override
                public void onFailure(Call<Collection<Medicament>> call, Throwable t) {
                    Log.e("error", t.getMessage());
                }
            });
        });
        dialogMedicament.show();
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
        fakPageFilterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null || editable.toString().isEmpty() || editable.toString().length() < 1) {
                    adapter.setStorage(ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicaments());
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setStorage(ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicamentsFiltered(editable.toString()));
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showCreateFAKDialogWindow() {
        dialog.setContentView(R.layout.create_medicament);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        EditText name = (EditText) dialog.findViewById(R.id.addMedicamentPageName);
        EditText desc = (EditText) dialog.findViewById(R.id.addMedicamentPageDescription);
        ImageButton qr = dialog.findViewById(R.id.addMedicamentPageQrScanner);
        EditText integerAmountValue = (EditText) dialog.findViewById(R.id.integerAmountValue);
        Button addButton = (Button) dialog.findViewById(R.id.addMedicamentPageCreateButton);
        EditText stringAmountValue = (EditText) dialog.findViewById(R.id.addMedicamentOtherAmountInput);
        CheckBox checkboxSpecifyOtherValue = (CheckBox) dialog.findViewById(R.id.checkboxSpecifyOtherValue);
        ImageButton increaseAmountButton = (ImageButton) dialog.findViewById(R.id.increaseAmountButton);
        ImageButton decreaseAmountButton = (ImageButton) dialog.findViewById(R.id.decreaseAmountButton);

        addButton.setOnClickListener(
                v -> {
                    String alertText = "Введите количество препарата";
                    if (name.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Введите название препарата", Toast.LENGTH_SHORT).show();
                    } else if (stringAmountValue.getVisibility() == View.GONE && (integerAmountValue.getText().toString().isEmpty() || Integer.parseInt(String.valueOf(integerAmountValue.getText())) < 1)) {
                        Toast.makeText(this, alertText, Toast.LENGTH_SHORT).show();
                    } else if (stringAmountValue.getVisibility() == View.VISIBLE && stringAmountValue.getText().toString().isEmpty()) {
                        Toast.makeText(this, alertText, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String nameFromEditText = name.getText().toString();
                        String descFromEditText = desc.getText().toString();
                        String amount = stringAmountValue.getVisibility() == View.VISIBLE ? stringAmountValue.getText().toString() :integerAmountValue.getText().toString();
                        MedicineOrganizerServerService.addMedicamentWithData(ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getId(),
                                nameFromEditText, descFromEditText, amount, new Callback<Collection<Medicament>>() {
                                    @Override
                                    public void onResponse(Call<Collection<Medicament>> call, Response<Collection<Medicament>> response) {
                                        adapter.setStorage(response.body());
                                        ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().setMedicaments(response.body());
                                        adapter.notifyDataSetChanged();
                                        setVisibilityIfMedicamentDataSetIsNotEmpty();
                                        dialog.cancel();
                                    }

                                    @Override
                                    public void onFailure(Call<Collection<Medicament>> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "Произошла ошибка при попытке добавления препарата в аптечку", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                }
        );

        checkboxSpecifyOtherValue.setOnClickListener(v->{
            if (checkboxSpecifyOtherValue.isChecked()) {
                increaseAmountButton.setVisibility(View.GONE);
                decreaseAmountButton.setVisibility(View.GONE);
                integerAmountValue.setVisibility(View.GONE);
                stringAmountValue.setVisibility(View.VISIBLE);
            } else {
                increaseAmountButton.setVisibility(View.VISIBLE);
                decreaseAmountButton.setVisibility(View.VISIBLE);
                integerAmountValue.setVisibility(View.VISIBLE);
                stringAmountValue.setVisibility(View.GONE);
            }
        });

        increaseAmountButton.setOnClickListener(v -> {
            if (integerAmountValue.getText().length() > 0) {
                integerAmountValue.setText(String.valueOf(Integer.parseInt(String.valueOf(integerAmountValue.getText())) + 1));
            } else {
                integerAmountValue.setText("1");
            }
        });

        decreaseAmountButton.setOnClickListener(v -> {
            if (integerAmountValue.getText().length() > 0 && Integer.parseInt(String.valueOf(integerAmountValue.getText())) > 0) {
                integerAmountValue.setText(String.valueOf(Integer.parseInt(String.valueOf(integerAmountValue.getText())) - 1));
            } else {
                integerAmountValue.setText("0");
            }
        });

        qr.setOnClickListener(v -> {
            startBarcodeScanner();

        });

        dialog.show();
    }

    private void startBarcodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("Пожалуйста, прицельтесь к штрихкоду на упаковке лекарства");
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                } else {
                    String barcode = result.getContents();
                    showProgressDialog();
                    addMedicamentByBarcodeServer(barcode);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

        }
    }

    private void addMedicamentByBarcodeServer(String barcode) {
        MedicineOrganizerServerService
                .setMedicamentByBarcode(ActiveFirstAidKitDataHolder
                        .getInstance().getFirstAidKit().getId(), barcode, new Callback<Collection<Medicament>>() {
                    @Override
                    public void onResponse(Call<Collection<Medicament>> call, Response<Collection<Medicament>> response) {            hideProgressDialog(); // Скрыть диалог загрузки после получения ответа от сервера
                        hideProgressDialog();
                        if (response.isSuccessful()) {
                            adapter.setStorage(response.body());
                            ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().setMedicaments(response.body());
                            adapter.notifyDataSetChanged();
                            setVisibilityIfMedicamentDataSetIsNotEmpty();
                            dialog.cancel();
                        }
                    }

                    @Override
                    public void onFailure(Call<Collection<Medicament>> call, Throwable t) {
                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(), "Произошла ошибка при попытке сканирования штрихкода", Toast.LENGTH_SHORT).show();

                    }
                });

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
        logout.setOnClickListener(v -> redirectActivity(FirstAitKitPage.this, LoginPageActivity.class));
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