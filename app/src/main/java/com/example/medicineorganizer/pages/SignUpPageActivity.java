package com.example.medicineorganizer.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.Retrofit.RetrofitMedicineOrganizerServerService;
import com.example.medicineorganizer.actions.MedicineOrganizerServerService;
import com.google.gson.Gson;

import java.io.IOException;

import dto.AppError;
import dto.RegUserDto;
import dto.UserDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPageActivity extends AppCompatActivity {

    private EditText username;
    private EditText user_email;
    private EditText user_password;
    private EditText password_confirm;
    private Button sign_up_button;
    private TextView go_to_login_page;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        sharedPreferences = getSharedPreferences("medicine_organizer_client_user_storage", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        username = (EditText) findViewById(R.id.signUpPageUsernameEditText);
        user_email = (EditText) findViewById(R.id.signUpPageUserEmailEditText);
        user_password = (EditText) findViewById(R.id.signUpPageUserPasswordEditText);
        password_confirm = (EditText) findViewById(R.id.signUpPageUserConfirmPasswordEditText);
        sign_up_button = (Button) findViewById(R.id.signUpPageSignUpButton);
        go_to_login_page = (TextView) findViewById(R.id.signUpPageGoToLoginPage);

        go_to_login_page.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, LoginPageActivity.class);
                    startActivity(intent);
                }
        );

        sign_up_button.setOnClickListener(
                v -> {
                    RetrofitMedicineOrganizerServerService.getInstance()
                            .getAuthApi()
                            .registration(new RegUserDto(username.getText().toString(), user_email.getText().toString(),
                                    user_password.getText().toString(), password_confirm.getText().toString()))
                            .enqueue(new Callback<UserDTO>() {
                                @Override
                                public void onResponse(@NonNull Call<UserDTO> call, @NonNull Response<UserDTO> response) {
                                    if (response.isSuccessful()) {
                                        if (MedicineOrganizerServerService.checkIfResponseBodyRegistrationIsEmpty(response)) {
                                            Log.e("server error", "Пришли некорректные данные");
                                            Toast.makeText(SignUpPageActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                                        } else {
                                            editor.putString("username", response.body().getUsername());
                                            editor.putLong("id", response.body().getId());
                                            editor.apply();
                                            Intent intent = new Intent(SignUpPageActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        if (response.errorBody() != null) {
                                            try {
                                                Gson gson = new Gson();
                                                AppError appError = gson.fromJson(response.errorBody().string(), AppError.class);
                                                Toast.makeText(SignUpPageActivity.this, appError.getMessage(), Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            Toast.makeText(SignUpPageActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<UserDTO> call, @NonNull Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                }
        );
    }
}