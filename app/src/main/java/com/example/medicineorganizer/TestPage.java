package com.example.medicineorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TestPage extends AppCompatActivity {

    private Button btn_go_to_main_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        btn_go_to_main_page = (Button)findViewById(R.id.button_go_to_main_page);

        btn_go_to_main_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(".MainActivity");
                startActivity(intent);
                Toast.makeText(TestPage.this, "Вы переключились на главную страницу",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }
}