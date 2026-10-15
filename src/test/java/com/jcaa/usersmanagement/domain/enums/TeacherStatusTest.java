package com.jcaa.usersmanagement.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidTeacherStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TeacherStatusTest {

    // --- Happy path ---

    @Test
    @DisplayName("Should return correct TeacherStatus for valid input")
    void shouldReturnCorrectTeacherStatusForValidInput() {
        assertEquals(TeacherStatus.ACTIVE, TeacherStatus.fromString("ACTIVE"));
        assertEquals(TeacherStatus.INACTIVE, TeacherStatus.fromString("INACTIVE"));
        assertEquals(TeacherStatus.PENDING, TeacherStatus.fromString("PENDING"));
        assertEquals(TeacherStatus.BLOCKED, TeacherStatus.fromString("BLOCKED"));
    }

    // --- Flujo excepciones y ramas de validación ---

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b", "INVALID_STATUS"})
    @DisplayName("Should throw InvalidTeacherStatusException when input is empty or invalid")
    void shouldThrowInvalidTeacherStatusExceptionWhenInputIsEmptyOrInvalid(final String input) {
        assertThrows(InvalidTeacherStatusException.class, () -> TeacherStatus.fromString(input));
    }
}