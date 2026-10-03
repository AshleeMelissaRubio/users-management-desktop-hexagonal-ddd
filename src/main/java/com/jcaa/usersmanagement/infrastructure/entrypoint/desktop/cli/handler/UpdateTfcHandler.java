package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TfcResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TfcController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TfcResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateTfcRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateTfcHandler implements OperationHandler {

    private final TfcController tfcController;
    private final ConsoleIO console;
    private final TfcResponsePrinter printer;

    @Override
    public void handle() {
        final String id          = console.readRequired("TFC ID         : ");
        final String title       = console.readRequired("New title      : ");
        final String description = console.readRequired("New description: ");
        final String advisorId   = console.readRequired("New advisor ID : ");

        try {
            final TfcResponse updated =
                    tfcController.updateTfc(new UpdateTfcRequest(id, title, description, advisorId));
            console.println("\n  TFC updated successfully.");
            printer.print(updated);
        } catch (final TfcNotFoundException exception) {
            console.println("  Not found: " + exception.getMessage());
        }
    }
}