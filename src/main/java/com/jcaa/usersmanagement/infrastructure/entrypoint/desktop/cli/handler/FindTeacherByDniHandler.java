package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TeacherResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TeacherController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TeacherResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FindTeacherByDniHandler implements OperationHandler {

    private final TeacherController teacherController;
    private final ConsoleIO console;
    private final TeacherResponsePrinter printer;

    @Override
    public void handle() {
        final String dni = console.readRequired("Teacher DNI: ");
        try {
            final TeacherResponse teacher = teacherController.findTeacherByDni(dni);
            printer.print(teacher);
        } catch (final TeacherNotFoundException exception) {
            console.println("  Not found: " + exception.getMessage());
        }
    }
}