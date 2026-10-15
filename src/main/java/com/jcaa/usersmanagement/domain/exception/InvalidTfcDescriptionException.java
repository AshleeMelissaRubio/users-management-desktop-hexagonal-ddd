package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTfcDescriptionException extends DomainException {

    private static final String MESSAGE_EMPTY = "The TFC description must not be empty.";
    private static final String MESSAGE_TOO_SHORT = "The tfc description must have at least %d characters.";

    private InvalidTfcDescriptionException(final String message) {
        super(message);
    }

    public static InvalidTfcDescriptionException becauseValueIsEmpty() {
        return new InvalidTfcDescriptionException(MESSAGE_EMPTY);
    }

    public static InvalidTfcDescriptionException becauseLengthIsTooShort(final int minimumLength) {
        return new InvalidTfcDescriptionException(String.format(MESSAGE_TOO_SHORT, minimumLength));
    }
}