package com.jcaa.usersmanagement.domain.exception;

public final class TeacherAlreadyExistsException extends DomainException {

    private static final String MESSAGE_DNI_EXISTS = "A teacher with dni '%s' already exists.";

    private TeacherAlreadyExistsException(final String message) {
        super(message);
    }

    public static TeacherAlreadyExistsException becauseDniAlreadyExists(final String dni) {
        return new TeacherAlreadyExistsException(String.format(MESSAGE_DNI_EXISTS, dni));
    }
}
