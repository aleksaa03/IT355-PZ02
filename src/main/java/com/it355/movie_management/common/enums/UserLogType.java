package com.it355.movie_management.common.enums;

public enum UserLogType {
    Click(0),
    Add(1),
    Update (2),
    Delete (3),
    Auth (4),
    Other (100);

    private final int value;

    UserLogType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}