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
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.actions.MedicineOrganizerServerService;
import com.example.medicineorganizer.data.ActiveFirstAidKitDataHolder;
import com.example.medicineorganizer.recyclerVies.MedicinesRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Collection;
import java.util.List;

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
    Dialog dialogMedicament;

    Dialog progressDialog;
    FirstAidKit firstAitKitFromStorage;

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
    }

    private void setVisibilityIfMedicamentDataSetIsNotEmpty() {
        if (ActiveFirstAidKitDataHolder.getInstance().getFirstAidKit().getMedicaments().size() > 0) {
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
    }

    private void showCreateFAKDialogWindow() {
        dialog.setContentView(R.layout.create_medicament);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        EditText name = (EditText) dialog.findViewById(R.id.addMedicamentPageName);
        EditText desc = (EditText) dialog.findViewById(R.id.addMedicamentPageDescription);
        ImageButton qr = dialog.findViewById(R.id.addMedicamentPageQrScanner);

        Button addButton = (Button) dialog.findViewById(R.id.addMedicamentPageCreateButton);

        addButton.setOnClickListener(
                v -> {
                    if (name.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Введите название препарата", Toast.LENGTH_SHORT).show();
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