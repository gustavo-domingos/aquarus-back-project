package com.gustavoalves.complementary_activities.utils.entities;

import lombok.Getter;

@Getter
public class DefaultError {
    private final String code;
    private final String message;

    public DefaultError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
