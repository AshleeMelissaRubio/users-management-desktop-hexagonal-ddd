package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.CreateTfcHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.DeleteTfcHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.FindTfcByIdHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.ListTfcsHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.OperationHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.UpdateTfcHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TfcResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu.TfcMenuOption;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TfcController;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TfcManagementCli {

    private static final String BANNER =
            """
            ==========================================
                   Graduation Projects System (TFC)
            ==========================================""";

    private static final String MENU_BORDER = "  ==========================================";

    private final TfcController tfcController;
    private final ConsoleIO console;

    public void start() {
        console.println(BANNER);
        final TfcResponsePrinter printer = new TfcResponsePrinter(console);
        runLoop(buildHandlers(printer));
    }

    private void runLoop(final Map<TfcMenuOption, OperationHandler> handlers) {
        boolean running = true;
        while (running) {
            printMenu();
            final int choice = console.readInt("\n  Option: ");
            final Optional<TfcMenuOption> option = TfcMenuOption.fromNumber(choice);

            if (option.isEmpty()) {
                console.println("  Invalid option. Please try again.");
            } else if (option.get() == TfcMenuOption.EXIT) {
                console.println("\n  Goodbye!\n");
                running = false;
            } else {
                executeHandler(handlers, option.get());
            }
        }
    }

    private void executeHandler(
            final Map<TfcMenuOption, OperationHandler> handlers, final TfcMenuOption option) {
        try {
            handlers.get(option).handle();
        } catch (final ConstraintViolationException exception) {
            console.println("  Validation errors:");
            exception.getConstraintViolations()
                    .forEach(violation -> console.println("    - " + violation.getMessage()));
        } catch (final RuntimeException exception) {
            console.println("  Unexpected error: " + exception.getMessage());
        }
    }

    private Map<TfcMenuOption, OperationHandler> buildHandlers(final TfcResponsePrinter printer) {
        return Map.of(
                TfcMenuOption.LIST_TFCS,  new ListTfcsHandler(tfcController, printer),
                TfcMenuOption.FIND_TFC,   new FindTfcByIdHandler(tfcController, console, printer),
                TfcMenuOption.CREATE_TFC, new CreateTfcHandler(tfcController, console, printer),
                TfcMenuOption.UPDATE_TFC, new UpdateTfcHandler(tfcController, console, printer),
                TfcMenuOption.DELETE_TFC, new DeleteTfcHandler(tfcController, console));
    }

    private void printMenu() {
        console.println();
        console.println(MENU_BORDER);
        console.println("    TFC Main Menu");
        console.println(MENU_BORDER);
        for (final TfcMenuOption option : TfcMenuOption.values()) {
            console.printf("    [%d] %s%n", option.getNumber(), option.getDescription());
        }
        console.println(MENU_BORDER);
    }
}