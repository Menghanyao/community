package com.example.community.exception;

public enum CustomizeErrorCode implements iCustomizeErrorCode {
    QUESTION_NOT_FOUND("你似乎来到了没有知识的荒原");

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(String message) {
        this.message = message;
    }
}
