package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TfcMenuOption {
    LIST_TFCS(1, "List all TFCs"),
    FIND_TFC(2, "Find TFC by ID"),
    CREATE_TFC(3, "Create TFC"),
    UPDATE_TFC(4, "Update TFC"),
    DELETE_TFC(5, "Delete TFC"),
    EXIT(6, "Exit");

    private final int number;
    private final String description;

    public static Optional<TfcMenuOption> fromNumber(final int number) {
        return Arrays.stream(values())
                .filter(option -> option.getNumber() == number)
                .findFirst();
    }
}