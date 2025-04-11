package com.example.questionariologicando.domain;

public class QuestionText extends Question {
    private String answer;
    QuestionText(String text) {
        super(text);
    }

    QuestionText(String text, String answer) {
        super(text);
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }
}
