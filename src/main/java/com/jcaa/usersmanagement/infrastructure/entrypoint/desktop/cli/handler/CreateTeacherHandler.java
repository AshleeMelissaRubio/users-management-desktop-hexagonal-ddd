package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.TeacherAlreadyExistsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.OperationHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TeacherResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TeacherController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateTeacherRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TeacherResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateTeacherHandler implements OperationHandler {

    private final TeacherController teacherController;
    private final ConsoleIO console;
    private final TeacherResponsePrinter printer;

    @Override
    public void handle() {
        final String id      = console.readRequired("ID     : ");
        final String dni     = console.readRequired("DNI    : ");
        final String name    = console.readRequired("Name   : ");
        final String address = console.readRequired("Address: ");

        try {
            final TeacherResponse created =
                    teacherController.createTeacher(new CreateTeacherRequest(id, dni, name, address));
            console.println("\n  Teacher created successfully.");
            printer.print(created);
        } catch (final TeacherAlreadyExistsException exception) {
            console.println("  Error: " + exception.getMessage());
        }
    }
}