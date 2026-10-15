package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TeacherResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TeacherResponsePrinter {

    private static final String SEPARATOR = "-".repeat(52);
    private static final String ROW_FORMAT = "  %-10s : %s%n";

    private final ConsoleIO console;

    public void print(final TeacherResponse response) {
        console.println(SEPARATOR);
        console.printf(ROW_FORMAT, "ID",      response.id());
        console.printf(ROW_FORMAT, "DNI",     response.dni());
        console.printf(ROW_FORMAT, "Name",    response.name());
        console.printf(ROW_FORMAT, "Address", response.address());
        console.printf(ROW_FORMAT, "Status",  response.status());
        console.println(SEPARATOR);
    }

    public void printList(final List<TeacherResponse> teachers) {
        if (teachers.isEmpty()) {
            console.println("  No teachers found.");
            return;
        }
        console.printf("%n  Total: %d teacher(s)%n", teachers.size());
        teachers.forEach(this::print);
    }
}