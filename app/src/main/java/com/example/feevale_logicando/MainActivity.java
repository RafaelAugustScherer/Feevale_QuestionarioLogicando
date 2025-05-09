package com.example.feevale_logicando;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feevale_logicando.domain.Choice;
import com.example.feevale_logicando.domain.Form;
import com.example.feevale_logicando.domain.Question;
import com.example.feevale_logicando.domain.QuestionMultipleChoice;
import com.example.feevale_logicando.domain.QuestionSingleChoice;
import com.example.feevale_logicando.domain.QuestionText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout questionContainer = findViewById(R.id.question_container);

        TextView formTitle = new TextView(this);
        formTitle.setTextSize(20);
        formTitle.setText(form.getTitle());
        formTitle.setTextColor(Color.BLACK);
        questionContainer.addView(formTitle);


        for (Question question : form.getQuestions()) {


            TextView questionText = new TextView(this);
            questionText.setText(question.getText());
            questionText.setTextColor(Color.DKGRAY);
            questionText.setTextSize(16);
            questionContainer.addView(questionText);

            if (question instanceof QuestionText) {
                EditText answerEditText = new EditText(this);
                questionContainer.addView(answerEditText);

                answerEditText.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        String answer = answerEditText.getText().toString();
                        Toast.makeText(MainActivity.this, "Resposta: " + answer, Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (question instanceof QuestionSingleChoice) {
                QuestionSingleChoice singleChoice = (QuestionSingleChoice) question;
                RadioGroup radioGroup = new RadioGroup(this);

                for (Choice choice : singleChoice.getChoices()) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(choice.getText());
                    radioGroup.addView(radioButton);
                }

                questionContainer.addView(radioGroup);

                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton selectedRadioButton = findViewById(checkedId);
                    if (selectedRadioButton != null) {
                        String answer = selectedRadioButton.getText().toString();
                        Toast.makeText(MainActivity.this, "Escolha única selecionada: " + answer, Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (question instanceof QuestionMultipleChoice) {
                QuestionMultipleChoice multipleChoice = (QuestionMultipleChoice) question;

                for (Choice choice : multipleChoice.getChoices()) {
                    CheckBox checkBox = new CheckBox(this);
                    checkBox.setText(choice.getText());
                    questionContainer.addView(checkBox);

                    checkBox.setOnClickListener(v -> {
                        CheckBox checkBoxView = (CheckBox) v;
                        if (checkBoxView.isChecked()) {
                            Toast.makeText(MainActivity.this, "Selecionado: " + checkBoxView.getText(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Desmarcado: " + checkBoxView.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    //Text
    QuestionText questionText = new QuestionText(1, "Porque usar Android Studio?", "text");

   //Single
    Choice choicesingle1 = new Choice(1, "Sim");
    Choice choicesingle2 = new Choice(2, "Não");
    Choice choicesingle3 = new Choice(3, "Talvez");

    Choice[] choicessingle = new Choice[]{choicesingle1, choicesingle2, choicesingle3};
    Question questionSingle = new QuestionSingleChoice(2, "Usaria Android Studio futuramente?", "Single",choicessingle);


    //Multiple
    Choice choicemultiple1= new Choice(1, "Flexibilidade");
    Choice choicemultiple2 = new Choice(2, "Layout Bonito");
    Choice choicemultiple3 = new Choice(3, "Facilidade de uso");

    Choice[] choicesMultiple = new Choice[]{choicemultiple1, choicemultiple2, choicemultiple3};
    Question questionMultiple = new QuestionMultipleChoice(3,"Quais são os pontos que mais gosta no Android Studio?", "Multiple", choicesMultiple);

    Question[] questions = new Question[]{questionText, questionMultiple, questionSingle};
    Form form = new Form(
            "Primeiro teste dos guri",
            new Date(),  // Data de início (hoje)
            new Date(System.currentTimeMillis() + 86400000L),  // Data de término (1 dia depois, ou seja, amanhã)
            questions
    );
    private QuestionSingleChoice createSingleChoiceQuestion() {
        Choice choicesingle1 = new Choice(1, "Sim");
        Choice choicesingle2 = new Choice(2, "Não");
        Choice choicesingle3 = new Choice(3, "Talvez");

        Choice[] choices = new Choice[]{choicesingle1, choicesingle2, choicesingle3};
        return new QuestionSingleChoice(2, "Usaria Android Studio futuramente?", "single", choices);
    }

    private QuestionMultipleChoice createMultipleChoiceQuestion() {
        Choice choice1 = new Choice(1, "Flexibilidade");
        Choice choice2 = new Choice(2, "Layout Bonito");
        Choice choice3 = new Choice(3, "Facilidade de uso");

        Choice[] choices = new Choice[]{choice1, choice2, choice3};
        return new QuestionMultipleChoice(3, "Quais são os pontos que mais gosta no Android Studio?", "multiple", choices);
    }

    private void styleTextView(TextView textView) {
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(18f);
        textView.setPadding(0, 20, 0, 10);
    }

    private void styleEditText(EditText editText) {
        editText.setBackgroundResource(android.R.drawable.edit_text);
        editText.setPadding(20, 10, 20, 10);
        editText.setTextColor(Color.BLACK);
        editText.setHintTextColor(Color.GRAY);
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