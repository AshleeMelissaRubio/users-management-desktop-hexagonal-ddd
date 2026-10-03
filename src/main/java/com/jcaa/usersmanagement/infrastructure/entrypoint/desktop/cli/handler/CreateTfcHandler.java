package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.TfcAlreadyExistsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TfcResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TfcController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateTfcRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TfcResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateTfcHandler implements OperationHandler {

    private final TfcController tfcController;
    private final ConsoleIO console;
    private final TfcResponsePrinter printer;

    @Override
    public void handle() {
        final String id          = console.readRequired("ID         : ");
        final String title       = console.readRequired("Title      : ");
        final String description = console.readRequired("Description: ");
        final String advisorId   = console.readRequired("Advisor ID : ");

        try {
            final TfcResponse created =
                    tfcController.createTfc(new CreateTfcRequest(id, title, description, advisorId));
            console.println("\n  TFC created successfully.");
            printer.print(created);
        } catch (final TfcAlreadyExistsException exception) {
            console.println("  Error: " + exception.getMessage());
        }
    }
}