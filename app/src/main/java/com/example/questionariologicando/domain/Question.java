package com.example.questionariologicando.domain;

public class Question {
    private String text;

    Question(String text) {
        this.text = text;
    }
    public String getText() {
        return this.text;
    }
}
