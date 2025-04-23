package com.studentinfo.exception;

public class StudentInfoException extends RuntimeException {
    public StudentInfoException(String message) {
        super(message);
    }

    public StudentInfoException(String message, Throwable cause) {
        super(message, cause);
    }
} 