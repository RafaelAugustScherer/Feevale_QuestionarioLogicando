package com.example.feevale_logicando.domain;

public class Choice {
    private final int id;
    public String text;

    public Choice(int id, String text) {
        this.text = text;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
