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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FormListActivity extends AppCompatActivity {

    private LinearLayout formListContainer;
    private final ArrayList<Form> formList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_list);

        formListContainer = findViewById(R.id.formListContainer);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FormService formService = new FormService(db);

        formService.getAvailableForms(dataSnapshot -> {
            for (QueryDocumentSnapshot document : dataSnapshot) {
                Map<String, Object> data = document.getData();
                Form form = Form.fromData(data);
                if (form != null) {
                    formList.add(form);
                    addFormButton(form);
                }
            }

            if (formList.isEmpty()) {
                Toast.makeText(this, "Nenhum formulário disponível no momento.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addFormButton(Form form) {
        Button button = new Button(this);
        button.setText(form.getTitle());
        button.setOnClickListener(v -> {
            Intent intent = new Intent(FormListActivity.this, MainActivity.class);
            intent.putExtra("formTitle", form.getTitle());
            startActivity(intent);
        });
        formListContainer.addView(button);
    }
}
