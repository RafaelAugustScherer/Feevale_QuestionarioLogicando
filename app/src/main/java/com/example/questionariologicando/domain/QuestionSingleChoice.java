package com.example.questionariologicando.domain;

public class QuestionSingleChoice extends Question {
    private Choice[] choices;
    private Choice selectedChoice;

    QuestionSingleChoice(String text, Choice[] choices) {
        super(text);
        this.choices = choices;
    }

    QuestionSingleChoice(String text, Choice[] choices, Choice selectedChoice) {
        super(text);
        this.choices = choices;
        this.selectedChoice = selectedChoice;
    }

    public Choice[] getChoices() {
        return this.choices;
    }

    public Choice getSelectedChoice() {
        return this.selectedChoice;
    }

    public String getChoiceTextByIndex(int index) {
        return this.choices[index].text;
    }
}
