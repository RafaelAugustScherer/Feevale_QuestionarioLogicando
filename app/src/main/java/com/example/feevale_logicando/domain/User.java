package com.example.feevale_logicando.domain;

import com.example.feevale_logicando.domain.enums.UserType;

public class User {
    private String name;
    private UserType type;

    public User(String name, UserType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public UserType getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
