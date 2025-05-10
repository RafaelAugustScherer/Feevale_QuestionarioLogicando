package com.example.feevale_logicando.domain;

public class QuestionSingleChoice extends Question {
    private Choice[] choices;
    private int selectedChoice;

    public QuestionSingleChoice(int id, String text, String type, Choice[] choices) {
        super(id, text, type);
        this.choices = choices;
    }

    public Choice[] getChoices() {
        return this.choices;
    }

    public int getSelectedChoice() {
        return this.selectedChoice;
    }

    public void setSelectedChoice(int selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    public String getChoiceTextByIndex(int index) {
        return this.choices[index].text;
    }
}
