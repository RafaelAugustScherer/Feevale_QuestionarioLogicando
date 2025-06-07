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

import java.util.ArrayList;
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

    public List<Question> generateAndValidateAnswers() {
        boolean isError = false;

        for (Question q : questions) {
            int qId = q.getId();
            TextView label = questionLabels.get(qId);
            label.setTextColor(Color.DKGRAY);

            try {
                if (q instanceof QuestionText) {
                    String text = textInputs.get(qId).getText().toString().trim();
                    if (text.isEmpty()) {
                        label.setTextColor(Color.RED);
                        isError = true;
                        continue;
                    }
                    ((QuestionText) q).setAnswer(text);
                } else if (q instanceof QuestionSingleChoice) {
                    RadioGroup rg = radioInputs.get(qId);
                    int selected = rg.getCheckedRadioButtonId();
                    if (selected == -1) {
                        label.setTextColor(Color.RED);
                        isError = true;
                        continue;
                    }
                    RadioButton selectedButton = rg.findViewById(selected);
                    ((QuestionSingleChoice) q).setSelectedChoice((int) selectedButton.getTag());
                } else if (q instanceof QuestionMultipleChoice) {
                    ((QuestionMultipleChoice) q).cleanChoices();
                    for (CheckBox cb : multiInputs.get(qId)) {
                        if (cb.isChecked()) {
                            ((QuestionMultipleChoice) q).addSelectedChoice((int) cb.getTag());
                        }
                    }
                    if (((QuestionMultipleChoice) q).getSelectedChoices().isEmpty()) {
                        label.setTextColor(Color.RED);
                        isError = true;
                    }
                }
            } catch (Exception e) {
                Log.e("ANSWER_PROCESSOR", "Erro na pergunta " + qId, e);
            }
        }

        if (isError) {
            return null;
        }

        return questions;
    }
}

