package com.example.feevale_logicando.domain;

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
}
