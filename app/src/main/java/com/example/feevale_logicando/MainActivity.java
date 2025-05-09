package com.example.feevale_logicando;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feevale_logicando.domain.Choice;
import com.example.feevale_logicando.domain.QuestionMultipleChoice;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<QuestionMultipleChoice> questions = new ArrayList<>();
        questions.add(createSampleQuestion());


        LinearLayout questionContainer = findViewById(R.id.question_container);


        for (QuestionMultipleChoice question : questions) {

            TextView questionText = new TextView(this);
            questionText.setText(question.getText());
            questionContainer.addView(questionText);


            for (Choice choice : question.getChoices()) {
                CheckBox choiceCheckBox = new CheckBox(this);
                choiceCheckBox.setText(choice.getText());

                choiceCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((CheckBox) v).isChecked()) {
                            Toast.makeText(MainActivity.this, "Selecionado: " + choice.getText(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Desmarcado: " + choice.getText(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                questionContainer.addView(choiceCheckBox);
            }
        }
    }

    // Método para criar uma pergunta de múltipla escolha de exemplo
    private QuestionMultipleChoice createSampleQuestion() {

        Choice choice1 = new Choice(1, "Escolha 1");
        Choice choice2 = new Choice(2, "Escolha 2");
        Choice choice3 = new Choice(3, "Escolha 3");


        Choice[] choices = new Choice[]{choice1, choice2, choice3};
        return new QuestionMultipleChoice(1, "Quais opções você escolhe?", "multiple", choices);
    }
}


//package com.example.feevale_logicando;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.feevale_logicando.domain.Form;
//import com.example.feevale_logicando.service.FormService;
//import com.example.feevale_logicando.service.OnGetDataListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.Map;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        FormService formService = new FormService(db);
//
//        ArrayList<Form> formList = new ArrayList<>();
//        formService.getAvailableForms(new OnGetDataListener() {
//            @Override
//            public void onSuccess(QuerySnapshot dataSnapshot) {
//                Log.d("TEST", dataSnapshot.toString());
//                for (QueryDocumentSnapshot document : dataSnapshot) {
//                    Map<String, Object> data = document.getData();
//                    formList.add(Form.fromData(data));
//                }
//                Log.d("TEST", String.format("List size: %d", formList.size()));
//            }
//        });
//    }
//}