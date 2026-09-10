package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu;

import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TeacherMenuOption {

    LIST_TEACHERS(1, "List all teachers"),
    FIND_BY_ID(2, "Find teacher by ID"),
    FIND_BY_DNI(3, "Find teacher by DNI"),
    CREATE_TEACHER(4, "Create teacher"),
    UPDATE_TEACHER(5, "Update teacher"),
    DELETE_TEACHER(6, "Delete teacher"),
    EXIT(0, "Exit");

    private final int number;
    private final String description;

    public static Optional<TeacherMenuOption> fromNumber(final int number) {
        for (final TeacherMenuOption option : values()) {
            if (option.number == number) {
                return Optional.of(option);
            }
        }
        return Optional.empty();
    }
}
