package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTfcIdException extends DomainException {

    private static final String MESSAGE_EMPTY = "The TFC ID must not be empty.";

    private InvalidTfcIdException(final String message) {
        super(message);
    }

    public static InvalidTfcIdException becauseValueIsEmpty() {
        return new InvalidTfcIdException(MESSAGE_EMPTY);
    }
}