package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidTeacherDniException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TeacherDniTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @ValueSource(strings = {"12345678A", "   12345678A   ", "12345678A \t"})
    @DisplayName("Should create TeacherDni with trimmed value when valid")
    void shouldCreateTeacherDniWithTrimmedValue(final String dni) {
        // Arrange
        final String correctDni = "12345678A";
        // Act
        final TeacherDni teacherDni = new TeacherDni(dni);
        // Assert
        assertEquals(correctDni, teacherDni.toString());
    }

    // --- Flujo con excepciones y ramas de validación ---

    @Test
    @DisplayName("Should throw NullPointerException when TeacherDni is null")
    void shouldThrowNullPointerExceptionWhenTeacherDniIsNull() {
        assertThrows(NullPointerException.class, () -> new TeacherDni(null));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "",
                    "   ",
                    "\t",
                    "\n",
                    "\r",
                    "\f",
                    "\b",
                    "1234",
                    "WRONG",
                    "123456"
            })
    @DisplayName("Should throw InvalidTeacherDniException when input is empty or invalid format")
    void shouldThrowInvalidTeacherDniExceptionWhenInputIsEmptyOrInvalid(final String dni) {
        assertThrows(InvalidTeacherDniException.class, () -> new TeacherDni(dni));
    }
}