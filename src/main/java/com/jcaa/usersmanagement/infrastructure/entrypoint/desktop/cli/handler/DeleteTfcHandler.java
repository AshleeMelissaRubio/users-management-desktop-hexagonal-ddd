package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TfcController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteTfcHandler implements OperationHandler {

    private final TfcController tfcController;
    private final ConsoleIO console;

    @Override
    public void handle() {
        final String id = console.readRequired("TFC ID to delete: ");
        try {
            tfcController.deleteTfc(id);
            console.println("  TFC deleted successfully.");
        } catch (final TfcNotFoundException exception) {
            console.println("  Not found: " + exception.getMessage());
        }
    }
}