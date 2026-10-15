package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTfcTitleException extends DomainException {

    private static final String MESSAGE_EMPTY = "The TFC title must not be empty.";
    private static final String MESSAGE_TOO_SHORT = "The tfc title must have at least %d characters.";

    private InvalidTfcTitleException(final String message) {
        super(message);
    }

    public static InvalidTfcTitleException becauseValueIsEmpty() {
        return new InvalidTfcTitleException(MESSAGE_EMPTY);
    }

    public static InvalidTfcTitleException becauseLengthIsTooShort(final int minimumLength) {
        return new InvalidTfcTitleException(String.format(MESSAGE_TOO_SHORT, minimumLength));
    }
}