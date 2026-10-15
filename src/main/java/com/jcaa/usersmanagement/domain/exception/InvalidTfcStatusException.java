package com.jcaa.usersmanagement.domain.exception;

import com.jcaa.usersmanagement.domain.enums.TfcStatus;

public final class InvalidTfcStatusException extends DomainException {

    private static final String MESSAGE_INVALID = "The provided TFC status is invalid.";
    private static final String MESSAGE_CANNOT_TRANSITION = "Cannot transition TFC status from %s to %s.";

    private InvalidTfcStatusException(final String message) {
        super(message);
    }

    public static InvalidTfcStatusException becauseValueIsInvalid() {
        return new InvalidTfcStatusException(MESSAGE_INVALID);
    }

    public static InvalidTfcStatusException becauseValueCannotTransition (TfcStatus currentStatus, TfcStatus targetStatus) {
        return new InvalidTfcStatusException(String.format(MESSAGE_CANNOT_TRANSITION, currentStatus, targetStatus));
    }
}