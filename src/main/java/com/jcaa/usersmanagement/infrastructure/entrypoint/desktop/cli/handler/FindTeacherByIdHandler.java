package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TeacherResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TeacherController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TeacherResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FindTeacherByIdHandler implements OperationHandler {

    private final TeacherController teacherController;
    private final ConsoleIO console;
    private final TeacherResponsePrinter printer;

    @Override
    public void handle() {
        final String id = console.readRequired("Teacher ID: ");
        try {
            final TeacherResponse teacher = teacherController.findTeacherById(id);
            printer.print(teacher);
        } catch (final TeacherNotFoundException exception) {
            console.println("  Not found: " + exception.getMessage());
        }
    }
}