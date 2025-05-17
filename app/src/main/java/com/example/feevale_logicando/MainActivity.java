package com.example.feevale_logicando;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.feevale_logicando.domain.*;
import com.example.feevale_logicando.service.FormService;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import org.json.JSONObject;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private final List<Question> questionList = new ArrayList<>();
    private final Map<Integer, EditText> textInputs = new HashMap<>();
    private final Map<Integer, RadioGroup> singleChoices = new HashMap<>();
    private final Map<Integer, List<CheckBox>> multipleChoices = new HashMap<>();
    private final Map<Integer, TextView> questionLabels = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FormService formService = new FormService(db);

        String selectedFormTitle = getIntent().getStringExtra("formTitle");

        formService.getAvailableForms(dataSnapshot -> {
            for (QueryDocumentSnapshot document : dataSnapshot) {
                Map<String, Object> data = document.getData();
                Form form = Form.fromData(data);
                if (form != null && form.getTitle().equals(selectedFormTitle)) {
                    bootFormComponents(form);
                    break;
                }
            }
        });
    }

    protected void bootFormComponents(Form form) {
        LinearLayout questionContainer = findViewById(R.id.question_container);
        TextView formTitle = findViewById(R.id.form_title);
        formTitle.setText(form.getTitle());

        for (Question question : form.getQuestions()) {
            int qId = question.getId();
            questionList.add(question);

            CardView card = new CardView(this);
            card.setCardElevation(8);
            card.setRadius(16);
            card.setUseCompatPadding(true);

            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setPadding(32, 32, 32, 32);

            TextView questionText = new TextView(this);
            questionText.setText(question.getText());
            questionText.setTextColor(Color.DKGRAY);
            questionText.setTextSize(16f);
            cardLayout.addView(questionText);
            questionLabels.put(qId, questionText);

            if (question instanceof QuestionText) {
                EditText et = new EditText(this);
                textInputs.put(qId, et);
                cardLayout.addView(et);
            } else if (question instanceof QuestionSingleChoice) {
                RadioGroup rg = new RadioGroup(this);
                for (Choice choice : ((QuestionSingleChoice) question).getChoices()) {
                    RadioButton rb = new RadioButton(this);
                    rb.setText(choice.getText());
                    rb.setTag(choice.getId());
                    rg.addView(rb);
                }
                singleChoices.put(qId, rg);
                cardLayout.addView(rg);
            } else if (question instanceof QuestionMultipleChoice) {
                List<CheckBox> checkboxes = new ArrayList<>();
                for (Choice choice : ((QuestionMultipleChoice) question).getChoices()) {
                    CheckBox cb = new CheckBox(this);
                    cb.setText(choice.getText());
                    cb.setTag(choice.getId());
                    checkboxes.add(cb);
                    cardLayout.addView(cb);
                }
                multipleChoices.put(qId, checkboxes);
            }

            card.addView(cardLayout);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 32);
            questionContainer.addView(card, cardParams);
        }

        addSubmitButton(questionContainer);
    }

    private void addSubmitButton(LinearLayout container) {
        Button submitButton = new Button(this);
        submitButton.setText("Enviar Respostas");
        submitButton.setBackgroundColor(Color.parseColor("#00AEEF"));
        submitButton.setTextColor(Color.WHITE);
        submitButton.setPadding(24, 24, 24, 24);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 32, 0, 32);
        submitButton.setLayoutParams(params);

        submitButton.setOnClickListener(v -> {
            FormAnswerProcessor processor = new FormAnswerProcessor(
                    questionList, textInputs, singleChoices, multipleChoices, questionLabels
            );

            JSONObject answers = processor.generateAndValidateAnswers();

            if (answers == null) {
                Toast.makeText(this, "Por favor, responda todas as perguntas.", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("RESPOSTAS_JSON", answers.toString());
                Toast.makeText(this, "Formulário enviado com sucesso!", Toast.LENGTH_LONG).show();
            }
        });

        container.addView(submitButton);
    }
}