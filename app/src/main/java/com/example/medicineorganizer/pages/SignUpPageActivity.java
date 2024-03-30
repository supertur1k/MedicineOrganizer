package com.example.medicineorganizer.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.Retrofit.RetrofitMedicineOrganizerServerService;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
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
                                        if (response.body() == null || response.body().getEmail() == null || response.body().getId() == null ||
                                                response.body().getUsername().isEmpty()) {
                                            Log.e("server error", "Пришли некорректные данные");
                                            Toast.makeText(SignUpPageActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                                        };
                                        Intent intent = new Intent(SignUpPageActivity.this, MainActivity.class);
                                        startActivity(intent);
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