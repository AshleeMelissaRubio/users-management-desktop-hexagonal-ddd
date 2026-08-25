package com.jcaa.usersmanagement.domain.exception;

public class InvalidTeacherIdException extends RuntimeException {
    public InvalidTeacherIdException(String message) {
        super(message);
    }
}
