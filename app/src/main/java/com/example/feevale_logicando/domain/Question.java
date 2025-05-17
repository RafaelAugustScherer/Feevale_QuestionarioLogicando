package com.example.feevale_logicando.domain;

import com.example.feevale_logicando.adapter.FirebaseAdapter;
import com.example.feevale_logicando.domain.enums.QuestionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Question implements Serializable {
    private int id;
    private String text;
    private String type;

    Question(int id, String text, String type) {
        this.id = id;
        this.text = text;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public String getType() { return this.type; }

    @SuppressWarnings("unchecked")
    public static Question fromData(Map<String, Object> data) throws Exception {
        int questionId = FirebaseAdapter.handleIntValue(data, "questionId");
        String questionType = (String) data.get("type");

        if (questionType == null) {
            throw new Exception("questionType is null");
        }

        if (questionType.equals(QuestionType.TEXT.value)) {
            return new QuestionText(questionId, (String) data.get("text"), questionType);
        }

        ArrayList<Choice> choices = new ArrayList<>();

        List<Map<String, Object>> choicesData = (List<Map<String, Object>>) data.get("choices");

        if (choicesData == null) {
            throw new Exception("choices list is null");
        }

        for (Map<String, Object> choice : choicesData) {
            choices.add(new Choice(FirebaseAdapter.handleIntValue(choice, "choiceId"), (String) choice.get("text")));
        }

        if (questionType.equals(QuestionType.SINGLE_CHOICE.value)) {
            return new QuestionSingleChoice(questionId, (String) data.get("text"), questionType, choices.toArray(new Choice[0]));
        }
        if (questionType.equals(QuestionType.MULTIPLE_CHOICE.value)) {
            return new QuestionMultipleChoice(questionId, (String) data.get("text"), questionType, choices.toArray(new Choice[0]));
        }

        throw new Exception(String.format("Question type %s is invalid", questionType));
    }

    public abstract Map<String, Object> toAnswerData();
}
