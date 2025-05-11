package com.example.feevale_logicando;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.feevale_logicando.domain.Choice;
import com.example.feevale_logicando.domain.Form;
import com.example.feevale_logicando.domain.Question;
import com.example.feevale_logicando.domain.QuestionMultipleChoice;
import com.example.feevale_logicando.domain.QuestionSingleChoice;
import com.example.feevale_logicando.domain.QuestionText;
import com.example.feevale_logicando.service.FormService;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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
            CardView card = new CardView(this);
            card.setCardElevation(6);
            card.setRadius(12);
            card.setUseCompatPadding(true);
            card.setCardBackgroundColor(Color.WHITE);

            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setPadding(32, 32, 32, 32);

            TextView questionText = new TextView(this);
            questionText.setText(question.getText());
            questionText.setTextColor(Color.DKGRAY);
            questionText.setTextSize(16f);
            cardLayout.addView(questionText);

            if (question instanceof QuestionText) {
                EditText answerEditText = new EditText(this);
                styleEditText(answerEditText);
                cardLayout.addView(answerEditText);
            } else if (question instanceof QuestionSingleChoice) {
                RadioGroup radioGroup = new RadioGroup(this);
                for (Choice choice : ((QuestionSingleChoice) question).getChoices()) {
                    RadioButton rb = new RadioButton(this);
                    rb.setText(choice.getText());
                    radioGroup.addView(rb);
                }
                cardLayout.addView(radioGroup);
            } else if (question instanceof QuestionMultipleChoice) {
                for (Choice choice : ((QuestionMultipleChoice) question).getChoices()) {
                    CheckBox cb = new CheckBox(this);
                    cb.setText(choice.getText());
                    cardLayout.addView(cb);
                }
            }

            card.addView(cardLayout);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 32);
            questionContainer.addView(card, cardParams);
        }

        addSubmitButton(questionContainer);
    }

    private void styleEditText(EditText editText) {
        editText.setBackgroundResource(android.R.drawable.edit_text);
        editText.setPadding(24, 16, 24, 16);
        editText.setTextColor(Color.BLACK);
        editText.setHintTextColor(Color.GRAY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 8, 0, 16);
        editText.setLayoutParams(params);
    }

    private void addSubmitButton(LinearLayout container) {
        Button submitButton = new Button(this);
        submitButton.setText("Enviar Respostas");
        submitButton.setBackgroundColor(Color.parseColor("#00AEEF"));
        submitButton.setTextColor(Color.WHITE);
        submitButton.setAllCaps(false);
        submitButton.setPadding(32, 24, 32, 24);
        submitButton.setTextSize(16f);
        submitButton.setTypeface(null, Typeface.BOLD);Button btn = new Button(this);
        btn.setText("Enviar Respostas");
        btn.setBackgroundColor(Color.parseColor("#00AEEF"));
        btn.setTextColor(Color.WHITE);
        btn.setAllCaps(false);
        btn.setPadding(32, 24, 32, 24);
        btn.setTextSize(16f);
        btn.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 32, 0, 32);
        submitButton.setLayoutParams(params);

        submitButton.setOnClickListener(v ->
                Toast.makeText(this, "Formulário enviado!", Toast.LENGTH_LONG).show()
        );

        container.addView(submitButton);
    }
}


//
//    //Text
//    QuestionText questionText1 = new QuestionText(1, "Porque usar Android Studio?", "text");
//    QuestionText questionText2 = new QuestionText(4, "Porque usar Android Studio2?", "text");
//    //Single
//    Choice choicesingle1 = new Choice(1, "Sim");
//    Choice choicesingle2 = new Choice(2, "Não");
//    Choice choicesingle3 = new Choice(3, "Talvez");
//
//    Choice[] choicessingle = new Choice[]{choicesingle1, choicesingle2, choicesingle3};
//    Question questionSingle = new QuestionSingleChoice(2, "Usaria Android Studio futuramente?", "Single",choicessingle);
//
//
//    //Multiple
//    Choice choicemultiple1= new Choice(1, "Flexibilidade");
//    Choice choicemultiple2 = new Choice(2, "Layout Bonito");
//    Choice choicemultiple3 = new Choice(3, "Facilidade de uso");
//
//    Choice[] choicesMultiple = new Choice[]{choicemultiple1, choicemultiple2, choicemultiple3};
//    Question questionMultiple = new QuestionMultipleChoice(3,"Quais são os pontos que mais gosta no Android Studio?", "Multiple", choicesMultiple);
//
//    Question[] questions = new Question[]{questionText1, questionText2, questionMultiple, questionSingle};
//    Form form = new Form(
//            "Primeiro teste dos guri",
//            new Date(),
//            new Date(System.currentTimeMillis() + 86400000L),
//            questions
//    );


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