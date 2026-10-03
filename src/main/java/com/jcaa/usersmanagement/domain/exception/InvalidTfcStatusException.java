package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTfcStatusException extends DomainException {

    private static final String MESSAGE_INVALID = "The provided TFC status is invalid.";

    private InvalidTfcStatusException(final String message) {
        super(message);
    }

    public static InvalidTfcStatusException becauseValueIsInvalid() {
        return new InvalidTfcStatusException(MESSAGE_INVALID);
    }
}