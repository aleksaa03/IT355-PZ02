package com.it355.movie_management.common.enums;

public enum UserRole {
    Client(0),
    Admin(1);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UserRole fromNum(int num) {
        for (UserRole role : UserRole.values()) {
            if (role.value == num) {
                return role;
            }
        }

        throw new IllegalArgumentException("No UserRole with id " + num);
    }
}