package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TeacherResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TeacherController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TeacherResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateTeacherRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateTeacherHandler implements OperationHandler {

    private final TeacherController teacherController;
    private final ConsoleIO console;
    private final TeacherResponsePrinter printer;

    @Override
    public void handle() {
        final String id      = console.readRequired("Teacher ID                                    : ");
        final String dni     = console.readRequired("New DNI                                       : ");
        final String name    = console.readRequired("New name                                      : ");
        final String address = console.readRequired("New address                                   : ");
        final String status  = console.readRequired("Status (ACTIVE / INACTIVE / PENDING / BLOCKED): ");

        try {
            final TeacherResponse updated = teacherController.updateTeacher(
                    new UpdateTeacherRequest(id, dni, name, address, status));
            console.println("\n  Teacher updated successfully.");
            printer.print(updated);
        } catch (final TeacherNotFoundException exception) {
            console.println("  Not found: " + exception.getMessage());
        }
    }
}
