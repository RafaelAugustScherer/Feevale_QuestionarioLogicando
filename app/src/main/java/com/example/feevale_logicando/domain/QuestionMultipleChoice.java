package com.example.feevale_logicando.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionMultipleChoice extends Question {
    private Choice[] choices;
    private List<Integer> selectedChoices;

    public QuestionMultipleChoice(int id, String text, String type, Choice[] choices) {
        super(id, text, type);
        this.choices = choices;
        this.selectedChoices = new ArrayList<>();
    }

    public Choice[] getChoices() {
        return this.choices;
    }

    public List<Integer> getSelectedChoices() {
        return selectedChoices;
    }

    public String getChoiceTextByIndex(int index) {
        return this.choices[index].text;
    }

    public void addSelectedChoice(int choiceId) {
        this.selectedChoices.add(choiceId);
    }

    public void cleanChoices() {
        this.selectedChoices = new ArrayList<>();
    }

    public Map<String, Object> toAnswerData() {
        Map<String, Object> questionData = new HashMap<>();

        questionData.put("questionId", this.getId());
        questionData.put("selectedChoices", this.getSelectedChoices());

        return questionData;
    }
}
