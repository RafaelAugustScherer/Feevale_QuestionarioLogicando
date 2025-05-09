package com.example.feevale_logicando.domain.enums;

public enum QuestionType {
    TEXT("text"),
    SINGLE_CHOICE("single"),
    MULTIPLE_CHOICE("multiple");

    public final String value;

    private QuestionType(String value) {
        this.value = value;
    }
}
