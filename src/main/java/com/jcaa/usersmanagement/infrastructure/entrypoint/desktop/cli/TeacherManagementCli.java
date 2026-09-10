package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.CreateTeacherHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.DeleteTeacherHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.FindTeacherByDniHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.FindTeacherByIdHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.ListTeachersHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.OperationHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.UpdateTeacherHandler;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TeacherResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu.TeacherMenuOption;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TeacherController;
import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TeacherManagementCli {

    private static final String BANNER =
            """
            ==========================================
                Teachers Management System
            =========================================""";

    private static final String MENU_BORDER = "  ==========================================";

    private final TeacherController teacherController;
    private final ConsoleIO console;

    public void start() {
        console.println(BANNER);
        final TeacherResponsePrinter printer = new TeacherResponsePrinter(console);
        runLoop(buildHandlers(printer));
    }

    private void runLoop(final Map<TeacherMenuOption, OperationHandler> handlers) {
        boolean running = true;
        while (running) {
            printMenu();
            final int choice = console.readInt("\n  Option: ");
            final Optional<TeacherMenuOption> option = TeacherMenuOption.fromNumber(choice);

            if (option.isEmpty()) {
                console.println("  Invalid option. Please try again.");
            } else if (option.get() == TeacherMenuOption.EXIT) {
                console.println("\n  Goodbye!\n");
                running = false;
            } else {
                executeHandler(handlers, option.get());
            }
        }
    }

    private void executeHandler(
            final Map<TeacherMenuOption, OperationHandler> handlers, final TeacherMenuOption option) {
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

    private Map<TeacherMenuOption, OperationHandler> buildHandlers(final TeacherResponsePrinter printer) {
        return Map.of(
                TeacherMenuOption.LIST_TEACHERS,    new ListTeachersHandler(teacherController, printer),
                TeacherMenuOption.FIND_BY_ID,       new FindTeacherByIdHandler(teacherController, console, printer),
                TeacherMenuOption.FIND_BY_DNI,      new FindTeacherByDniHandler(teacherController, console, printer),
                TeacherMenuOption.CREATE_TEACHER,   new CreateTeacherHandler(teacherController, console, printer),
                TeacherMenuOption.UPDATE_TEACHER,   new UpdateTeacherHandler(teacherController, console, printer),
                TeacherMenuOption.DELETE_TEACHER,   new DeleteTeacherHandler(teacherController, console)
        );
    }

    private void printMenu() {
        console.println();
        console.println(MENU_BORDER);
        console.println("    Teachers Menu");
        console.println(MENU_BORDER);
        for (final TeacherMenuOption option : TeacherMenuOption.values()) {
            console.printf("    [%d] %s%n", option.getNumber(), option.getDescription());
        }
        console.println(MENU_BORDER);
    }
}
