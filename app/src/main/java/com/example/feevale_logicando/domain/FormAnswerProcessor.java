package com.example.feevale_logicando.domain;

import android.graphics.Color;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;

public class FormAnswerProcessor {
    private final List<Question> questions;
    private final Map<Integer, EditText> textInputs;
    private final Map<Integer, RadioGroup> radioInputs;
    private final Map<Integer, List<CheckBox>> multiInputs;
    private final Map<Integer, TextView> questionLabels;

    public FormAnswerProcessor(List<Question> questions,
                               Map<Integer, EditText> textInputs,
                               Map<Integer, RadioGroup> radioInputs,
                               Map<Integer, List<CheckBox>> multiInputs,
                               Map<Integer, TextView> questionLabels) {
        this.questions = questions;
        this.textInputs = textInputs;
        this.radioInputs = radioInputs;
        this.multiInputs = multiInputs;
        this.questionLabels = questionLabels;
    }

    public JSONObject generateAndValidateAnswers() {
        JSONObject answers = new JSONObject();
        boolean allAnswered = true;

        for (Question q : questions) {
            int qId = q.getId();
            TextView label = questionLabels.get(qId);
            label.setTextColor(Color.DKGRAY);

            try {
                if (q instanceof QuestionText) {
                    String text = textInputs.get(qId).getText().toString().trim();
                    if (text.isEmpty()) {
                        label.setTextColor(Color.RED);
                        allAnswered = false;
                    }
                    answers.put(String.valueOf(qId), text);

                } else if (q instanceof QuestionSingleChoice) {
                    RadioGroup rg = radioInputs.get(qId);
                    int selected = rg.getCheckedRadioButtonId();
                    if (selected == -1) {
                        label.setTextColor(Color.RED);
                        allAnswered = false;
                        answers.put(String.valueOf(qId), JSONObject.NULL);
                    } else {
                        RadioButton selectedButton = rg.findViewById(selected);
                        answers.put(String.valueOf(qId), selectedButton.getTag());
                    }

                } else if (q instanceof QuestionMultipleChoice) {
                    JSONArray selectedIds = new JSONArray();
                    for (CheckBox cb : multiInputs.get(qId)) {
                        if (cb.isChecked()) {
                            selectedIds.put((int) cb.getTag());
                        }
                    }
                    if (selectedIds.length() == 0) {
                        label.setTextColor(Color.RED);
                        allAnswered = false;
                    }
                    answers.put(String.valueOf(qId), selectedIds);
                }
            } catch (Exception e) {
                Log.e("ANSWER_PROCESSOR", "Erro na pergunta " + qId, e);
            }
        }

        return allAnswered ? answers : null;
    }
}

