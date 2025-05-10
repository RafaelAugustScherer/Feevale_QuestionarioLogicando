package com.example.feevale_logicando;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FormListActivity extends AppCompatActivity {

    private Button formButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_list);

        formButton = findViewById(R.id.btnForm);

        formButton.setOnClickListener(view -> {
            // Vai para o formulário escolhido
            Intent intent = new Intent(FormListActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}