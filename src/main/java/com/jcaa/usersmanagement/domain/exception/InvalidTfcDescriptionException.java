package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTfcDescriptionException extends DomainException {

    private static final String MESSAGE_EMPTY = "The TFC description must not be empty.";

    private InvalidTfcDescriptionException(final String message) {
        super(message);
    }

    public static InvalidTfcDescriptionException becauseValueIsEmpty() {
        return new InvalidTfcDescriptionException(MESSAGE_EMPTY);
    }
}