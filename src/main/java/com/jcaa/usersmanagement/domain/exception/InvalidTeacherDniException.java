package com.jcaa.usersmanagement.domain.exception;

public final class InvalidTeacherDniException extends DomainException {

    private static final String MESSAGE_EMPTY = "The teacher dni must not be empty.";
    private static final String MESSAGE_TOO_SHORT = "The teacher dni must have at least %d characters.";

    private InvalidTeacherDniException(final String message) {
        super(message);
    }

    public static InvalidTeacherDniException becauseValueIsEmpty() {
        return new InvalidTeacherDniException(MESSAGE_EMPTY);
    }

    public static InvalidTeacherDniException becauseLengthIsTooShort(final int minimumLength) {
        return new InvalidTeacherDniException(String.format(MESSAGE_TOO_SHORT, minimumLength));
    }
}