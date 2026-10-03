package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TfcResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TfcResponsePrinter {

    private static final String SEPARATOR = "-".repeat(52);
    private static final String ROW_FORMAT = "  %-12s : %s%n";

    private final ConsoleIO console;

    public void print(final TfcResponse response) {
        console.println(SEPARATOR);
        console.printf(ROW_FORMAT, "ID",          response.id());
        console.printf(ROW_FORMAT, "Title",       response.title());
        console.printf(ROW_FORMAT, "Description", response.description());
        console.printf(ROW_FORMAT, "Advisor ID",  response.advisorId());
        console.printf(ROW_FORMAT, "Status",      response.status());
        console.println(SEPARATOR);
    }

    public void printList(final List<TfcResponse> tfcs) {
        if (tfcs.isEmpty()) {
            console.println("  No TFCs found.");
            return;
        }
        console.printf("%n  Total: %d TFC(s)%n", tfcs.size());
        tfcs.forEach(this::print);
    }
}