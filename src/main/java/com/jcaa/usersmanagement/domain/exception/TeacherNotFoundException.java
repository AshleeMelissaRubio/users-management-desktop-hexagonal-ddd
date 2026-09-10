package com.jcaa.usersmanagement.domain.exception;

public class TeacherNotFoundException extends RuntimeException {
    private static final String MESSAGE_BY_ID = "The teacher with id '%s' was not found.";

    private TeacherNotFoundException(final String message) {
        super(message);
    }

    public static TeacherNotFoundException becauseIdWasNotFound(final String TeacherId) {
        return new TeacherNotFoundException(String.format(MESSAGE_BY_ID, TeacherId));
    }

    public static TeacherNotFoundException becauseDniWasNotFound(final String TeacherDni) {
        return new TeacherNotFoundException(String.format("Teacher with DNI %s was not found.", TeacherDni));
    }
}
