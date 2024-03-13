package com.example.medicineorganizer.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medicineorganizer.R;
import com.example.medicineorganizer.actions.LoginActions;

public class LoginPageActivity extends AppCompatActivity {
    private EditText user_email;
    private EditText user_password;
    private Button login_button;
    private Button sign_up_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        user_email = (EditText) findViewById(R.id.userEmailLoginPageEditText);
        user_password = (EditText) findViewById(R.id.userPasswordLoginPageEditText);
        login_button = (Button) findViewById(R.id.buttonLogin);
        sign_up_button = (Button) findViewById(R.id.buttonSignUp);

        sign_up_button.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, SignUpPageActivity.class);
                    startActivity(intent);
                }
        );

        login_button.setOnClickListener(
                v -> {
                    String alertText = "Некорректные данные";
                    if (new LoginActions().isCredentialsAreValid(user_email.getText().toString(), user_password.getText().toString())) alertText = "Добро пожаловать";

                    if (alertText != null && !alertText.isEmpty()) {
                        Toast.makeText(
                                LoginPageActivity.this, alertText,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}