package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TfcResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TfcController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TfcResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListTfcsHandler implements OperationHandler {

    private final TfcController tfcController;
    private final TfcResponsePrinter printer;

    @Override
    public void handle() {
        final List<TfcResponse> tfcs = tfcController.listAllTfcs();
        printer.printList(tfcs);
    }
}