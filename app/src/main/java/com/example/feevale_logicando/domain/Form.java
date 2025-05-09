package com.example.feevale_logicando.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Form {
    public String name;
    public Date availableFrom;
    public Date availableUntil;
    public Question[] questions;


    public Form(String name, Date availableFrom, Date availableUntil, Question[] questions) {
        this.name = name;
        this.availableFrom = availableFrom;
        this.availableUntil = availableUntil;
        this.questions = questions;
    }

    public String getTitle() {
        return name;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public Date getAvailableUntil() {
        return availableUntil;
    }

    public Question[] getQuestions() {
        return questions;
    }

    @SuppressWarnings("unchecked")
    public static Form fromData(Map<String, Object> data) throws ClassCastException {
        try {
            List<Question> questions = new ArrayList<>();

            List<Map<String, Object>> questionsData = (List<Map<String, Object>>) data.get("questions");

            if (questionsData == null) {
                return null;
            }

            for (Map<String, Object> question : questionsData) {
                questions.add(Question.fromData(question));
            }

            return new Form(
                    (String) data.get("name"),
                    (Date) data.get("availableFrom"),
                    (Date) data.get("availableUntil"),
                    questions.toArray(new Question[0]));
        } catch (Exception e) {
            System.out.printf("Error reading form Data: %s", e.getMessage());
            return null;
        }
    }
}
