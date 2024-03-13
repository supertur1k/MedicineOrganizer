package com.example.medicineorganizer.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.medicineorganizer.R;

public class SignUpPageActivity extends AppCompatActivity {

    private EditText user_email;
    private EditText user_password;
    private EditText password_confirm;
    private Button sign_up_button;
    private Button go_to_login_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        user_email = (EditText) findViewById(R.id.userEmailSignUpPageEditText);
        user_password = (EditText) findViewById(R.id.userPasswordSignUpPageEditText);
        password_confirm = (EditText) findViewById(R.id.userPasswordConfirmEditText);
        sign_up_button = (Button) findViewById(R.id.signUpButton);
        go_to_login_page = (Button) findViewById(R.id.alreadyHaveAccountButton);

        go_to_login_page.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, LoginPageActivity.class);
                    startActivity(intent);
                }
        );
    }
}