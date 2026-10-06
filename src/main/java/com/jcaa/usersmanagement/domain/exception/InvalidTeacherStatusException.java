package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTeacherStatusException extends DomainException {

    private static final String MESSAGE_INVALID = "The teacher status '%s' is not valid.";

    private InvalidTeacherStatusException(final String message) {
        super(message);
    }

    public static InvalidTeacherStatusException becauseValueIsInvalid(final String status) {
        return new InvalidTeacherStatusException(String.format(MESSAGE_INVALID, status));
    }
}