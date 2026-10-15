package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTeacherNameException extends DomainException {

    private static final String MESSAGE_EMPTY = "The teacher name must not be empty.";
    private static final String MESSAGE_TOO_SHORT = "The teacher name must have at least %d characters.";

    private InvalidTeacherNameException(final String message) {
        super(message);
    }

    public static InvalidTeacherNameException becauseValueIsEmpty() {
        return new InvalidTeacherNameException(MESSAGE_EMPTY);
    }

    public static InvalidTeacherNameException becauseLengthIsTooShort(final int minimumLength) {
        return new InvalidTeacherNameException(String.format(MESSAGE_TOO_SHORT, minimumLength));
    }
}
