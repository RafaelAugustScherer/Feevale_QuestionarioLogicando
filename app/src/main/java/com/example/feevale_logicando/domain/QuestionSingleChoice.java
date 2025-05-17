package com.example.feevale_logicando.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QuestionSingleChoice extends Question {
    private final Choice[] choices;
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

    public void setSelectedChoice(String selectedChoice) {
        for (Choice choice: this.choices) {
            if (Objects.equals(choice.getText(), selectedChoice)) {
                this.selectedChoice = choice.getId();
                return;
            }
        }
    }

    public String getChoiceTextByIndex(int index) {
        return this.choices[index].text;
    }

    @Override
    public Map<String, Object> toAnswerData() {
        Map<String, Object> questionData = new HashMap<>();

        questionData.put("questionId", this.getId());
        questionData.put("selectedChoice", this.getSelectedChoice());

        return questionData;
    }
}
