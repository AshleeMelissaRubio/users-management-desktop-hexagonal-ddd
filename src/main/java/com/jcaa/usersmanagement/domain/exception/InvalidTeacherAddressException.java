package com.jcaa.usersmanagement.domain.exception;

public class InvalidTeacherAddressException extends DomainException {
    private static final String MESSAGE_EMPTY = "The teacher address must not be empty.";
    private static final String MESSAGE_INACTIVE = "The teacher address must not be empty.";
    private static final String MESSAGE_TOO_SHORT = "The teacher address must have at least %d characters.";


    private InvalidTeacherAddressException(final String message) {
        super(message);
    }

    public static InvalidTeacherAddressException becauseValueIsEmpty() {
        return new InvalidTeacherAddressException(MESSAGE_EMPTY);
    }

    public static InvalidTeacherAddressException becauseTeacherIsInactive() {
        return new InvalidTeacherAddressException(MESSAGE_INACTIVE);
    }

    public static InvalidTeacherAddressException becauseLengthIsTooShort(final int minimumLength) {
        return new InvalidTeacherAddressException(MESSAGE_TOO_SHORT, minimumLength);
    }
}
