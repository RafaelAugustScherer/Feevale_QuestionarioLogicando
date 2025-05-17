package com.example.feevale_logicando;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.feevale_logicando.service.FormService;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class WelcomeActivity extends AppCompatActivity {

    private EditText nameEditText;
    private Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        nameEditText = findViewById(R.id.editTextName);
        proceedButton = findViewById(R.id.btnProceed);

        proceedButton.setOnClickListener(view -> {
            String name = nameEditText.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Por favor, informe seu nome.", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FormService formService = new FormService(db);

            formService.getAvailableForms(dataSnapshot -> {
                if (dataSnapshot == null || dataSnapshot.isEmpty()) {
                    Toast.makeText(this, "Nenhum formulário disponível no momento.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, FormListActivity.class);
                    startActivity(intent);
                }
            });
        });
    }
}