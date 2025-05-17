package com.example.feevale_logicando.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormAnswer {
    String userName;
    Form formWithAnswers;

    public FormAnswer(String userName, Form formWithAnswers) {
        this.userName = userName;
        this.formWithAnswers = formWithAnswers;
    }

    public String getUserName() {
        return userName;
    }

    public Form getFormWithAnswers() {
        return formWithAnswers;
    }

    public Map<String, Object> toData() {
        Map<String, Object> formData = new HashMap<>();

        formData.put("userName", this.getUserName());

        List<Map<String, Object>> questionsData = new ArrayList<>();
        for (Question question: this.getFormWithAnswers().getQuestions()) {
            questionsData.add(question.toAnswerData());
        }
        formData.put("answers", questionsData);

        return formData;
    }
}
