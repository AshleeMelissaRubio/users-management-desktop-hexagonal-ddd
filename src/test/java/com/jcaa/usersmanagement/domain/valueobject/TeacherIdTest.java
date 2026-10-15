package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidTeacherIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TeacherIdTest {

    // --- Happy path ---

    @ParameterizedTest
    @ValueSource(strings = {" t-001 ", "  t-001  ", "t-001\t"})
    @DisplayName("Should create TeacherId with trimmed value")
    void shouldCreateTeacherIdWithTrimmedValue(final String input) {
        // Arrange
        final String correctTeacherId = "t-001";
        // Act
        final TeacherId teacherId = new TeacherId(input);
        // Assert
        assertEquals(correctTeacherId, teacherId.toString());
    }

    // --- Flujo con excepciones y ramas condicionales ---

    @Test
    @DisplayName("Should throw NullPointerException when TeacherId is null")
    void shouldThrowNullPointerExceptionWhenTeacherIdIsNull() {
        assertThrows(NullPointerException.class, () -> new TeacherId(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b"})
    @DisplayName("Should throw InvalidTeacherIdException when TeacherId is empty")
    void shouldThrowInvalidTeacherIdExceptionWhenTeacherIdIsEmpty(final String input) {
        // Act & Assert
        assertThrows(InvalidTeacherIdException.class, () -> new TeacherId(input));
    }
}