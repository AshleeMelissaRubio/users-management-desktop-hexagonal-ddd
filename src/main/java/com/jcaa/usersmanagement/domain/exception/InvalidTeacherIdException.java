package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTeacherIdException extends DomainException {

    private static final String MESSAGE_EMPTY = "The teacher id must not be empty.";

    private InvalidTeacherIdException(final String message) {
        super(message);
    }

    public static InvalidTeacherIdException becauseValueIsEmpty() {
        return new InvalidTeacherIdException(MESSAGE_EMPTY);
    }
}
