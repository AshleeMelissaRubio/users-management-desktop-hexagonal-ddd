package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TfcResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TfcController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TfcResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FindTfcByIdHandler implements OperationHandler {

    private final TfcController tfcController;
    private final ConsoleIO console;
    private final TfcResponsePrinter printer;

    @Override
    public void handle() {
        final String id = console.readRequired("TFC ID: ");
        try {
            final TfcResponse tfc = tfcController.findTfcById(id);
            printer.print(tfc);
        } catch (final TfcNotFoundException exception) {
            console.println("  Not found: " + exception.getMessage());
        }
    }
}