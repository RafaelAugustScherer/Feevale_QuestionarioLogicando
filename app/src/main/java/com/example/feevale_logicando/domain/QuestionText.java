package com.example.feevale_logicando.domain;

import java.util.HashMap;
import java.util.Map;

public class QuestionText extends Question {
    private String answer;

    public QuestionText(int id, String text, String type) {
        super(id, text, type);
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public Map<String, Object> toAnswerData() {
        Map<String, Object> questionData = new HashMap<>();

        questionData.put("questionId", this.getId());
        questionData.put("answer", this.getAnswer());

        return questionData;
    }
}
