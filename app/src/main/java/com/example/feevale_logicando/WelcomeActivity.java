package com.example.feevale_logicando;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feevale_logicando.domain.Form;
import com.example.feevale_logicando.service.FormService;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

            formService.getAvailableForms(formsData -> {
                if (formsData == null || formsData.isEmpty()) {
                    Toast.makeText(this, "Nenhum formulário disponível no momento.", Toast.LENGTH_LONG).show();
                } else {
                    List<Form> forms = new ArrayList<>();
                    for (Map<String, Object> formData: formsData) {
                        forms.add(Form.fromData(formData));
                    }

                    Intent intent = new Intent(WelcomeActivity.this, FormListActivity.class);
                    intent.putExtra("availableForms", forms.toArray(new Form[0]));
                    intent.putExtra("name", name);

                    startActivity(intent);
                }
            });
        });
    }
}