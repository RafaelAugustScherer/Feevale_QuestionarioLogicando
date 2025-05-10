package com.example.feevale_logicando;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            String name = nameEditText.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(this, "Por favor, informe seu nome.", Toast.LENGTH_SHORT).show();
            } else {

                //RAFAEL AQUI É O BOTÃO QUE FAZ COM QUE VAMOS PARA PRÓXIMA TELA
                //UMA COISA INTERESSANTE DE FAZERMOS É COLOCAR UMA CONDICIONAL PARA ELE VERIFICAR SE TEMOS FORMS DISPONÍVEIS
                Intent intent = new Intent(WelcomeActivity.this, FormListActivity.class);
                startActivity(intent);
            }
        });
    }
}