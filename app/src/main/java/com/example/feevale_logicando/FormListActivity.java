package com.example.feevale_logicando;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feevale_logicando.domain.Form;
import com.example.feevale_logicando.service.FormService;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class FormListActivity extends AppCompatActivity {
    private LinearLayout formListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_list);

        formListContainer = findViewById(R.id.formListContainer);

        Form[] forms = (Form[]) getIntent().getSerializableExtra("availableForms");

        if (forms == null || forms.length == 0) {
            Toast.makeText(this, "Nenhum formulário disponível no momento.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Form form: forms) {
            addFormButton(form);
        }
    }

    private void addFormButton(Form form) {
        Button button = new Button(this);
        button.setText(form.getTitle());
        button.setOnClickListener(v -> {
            Intent intent = new Intent(FormListActivity.this, MainActivity.class);
            intent.putExtra("selectedForm", form);
            startActivity(intent);
        });
        formListContainer.addView(button);
    }
}
