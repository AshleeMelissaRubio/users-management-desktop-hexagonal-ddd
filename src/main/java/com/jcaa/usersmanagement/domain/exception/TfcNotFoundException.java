package com.jcaa.usersmanagement.domain.exception;

public final class TfcNotFoundException extends DomainException {

    private static final String MESSAGE_BY_ID = "The graduation project with id '%s' was not found.";

    private TfcNotFoundException(final String message) {
        super(message);
    }

    public static TfcNotFoundException becauseIdWasNotFound(final String tfcId) {
        return new TfcNotFoundException(String.format(MESSAGE_BY_ID, tfcId));
    }
}
