package com.jcaa.usersmanagement.domain.exception;

public final class TfcAlreadyExistsException extends DomainException {

    private static final String MESSAGE_TITLE_EXISTS = "A graduation project with title '%s' already exists.";

    private TfcAlreadyExistsException(final String message) {
        super(message);
    }

    public static TfcAlreadyExistsException becauseTitleAlreadyExists(final String title) {
        return new TfcAlreadyExistsException(String.format(MESSAGE_TITLE_EXISTS, title));
    }
}
