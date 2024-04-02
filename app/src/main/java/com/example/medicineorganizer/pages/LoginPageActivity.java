package com.example.medicineorganizer.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.actions.MedicineOrganizerServerService;
import com.google.gson.Gson;

import java.io.IOException;

import dto.AppError;
import dto.JwtResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPageActivity extends AppCompatActivity {
    private EditText username;
    private EditText user_password;
    private Button login_button;
    private TextView go_to_sign_up_page_textview;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("medicine_organizer_client_user_storage", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username = (EditText) findViewById(R.id.loginPageUsernameEditText);
        user_password = (EditText) findViewById(R.id.loginPageUserPasswordEditText);
        login_button = (Button) findViewById(R.id.loginPageButtonLogin);
        go_to_sign_up_page_textview = (TextView) findViewById(R.id.loginPageGoToRegistrationPage);

        addListenerOnButton();

    }
    public void addListenerOnButton() {
        go_to_sign_up_page_textview.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, SignUpPageActivity.class);
                    startActivity(intent);
                }
        );

        login_button.setOnClickListener(
                v -> {
                    String editTextUsername = username.getText().toString();
                    String userTextPassword = user_password.getText().toString();
                    MedicineOrganizerServerService.authAndGetToken(editTextUsername, userTextPassword, new Callback<JwtResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<JwtResponse> call, @NonNull Response<JwtResponse> response) {
                            if (response.isSuccessful()) {
                                if (MedicineOrganizerServerService.checkIfResponseBodyWithTokenIsEmpty(response)) {
                                    Log.e("server error", "Не пришел токен");
                                    Toast.makeText(LoginPageActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                                } else {
                                    editor.putString("token", response.body().getToken());
                                    editor.putString("username", editTextUsername);
                                    editor.putLong("id", response.body().getId());
                                    editor.apply();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                if (response.errorBody() != null) {
                                    try {
                                        Gson gson = new Gson();
                                        AppError appError = gson.fromJson(response.errorBody().string(), AppError.class);
                                        Toast.makeText(LoginPageActivity.this, appError.getMessage(), Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(LoginPageActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<JwtResponse> call, @NonNull Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
        );
    }
}