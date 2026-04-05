package com.project.aiassistant.exception;

import lombok.Getter;

public class ApiException extends RuntimeException {
    @Getter
    private final int statusCode;

    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
