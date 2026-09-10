package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TeacherController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteTeacherHandler implements OperationHandler {

    private final TeacherController teacherController;
    private final ConsoleIO console;

    @Override
    public void handle() {
        final String id = console.readRequired("Teacher ID to delete: ");
        try {
            teacherController.deleteTeacher(id);
            console.println("  Teacher deleted successfully.");
        } catch (final TeacherNotFoundException exception) {
            console.println("  Not found: " + exception.getMessage());
        }
    }
}
