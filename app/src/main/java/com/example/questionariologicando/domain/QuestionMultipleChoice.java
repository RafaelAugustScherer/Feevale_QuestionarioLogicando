package com.example.questionariologicando.domain;

public class QuestionMultipleChoice extends Question {
    private Choice[] choices;
    private Choice[] selectedChoices;

    QuestionMultipleChoice(String text, Choice[] choices) {
        super(text);
        this.choices = choices;
    }

    QuestionMultipleChoice(String text, Choice[] choices, Choice[] selectedChoices) {
        super(text);
        this.choices = choices;
        this.selectedChoices = selectedChoices;
    }

    public Choice[] getChoices() {
        return this.choices;
    }

    public Choice[] getSelectedChoices() {
        return selectedChoices;
    }

    public String getChoiceTextByIndex(int index) {
        return this.choices[index].text;
    }
}
