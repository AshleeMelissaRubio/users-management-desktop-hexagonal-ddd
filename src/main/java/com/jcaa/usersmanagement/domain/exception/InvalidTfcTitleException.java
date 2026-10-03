package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTfcTitleException extends DomainException {

    private static final String MESSAGE_EMPTY = "The TFC title must not be empty.";

    private InvalidTfcTitleException(final String message) {
        super(message);
    }

    public static InvalidTfcTitleException becauseValueIsEmpty() {
        return new InvalidTfcTitleException(MESSAGE_EMPTY);
    }
}