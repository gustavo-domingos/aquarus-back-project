package com.gustavoalves.complementary_activities.domain.user;

public enum UserType {
    Student(0),
    Coordinator(1);

    public final int code;

    UserType(int code) {
        this.code = code;
    }
}

