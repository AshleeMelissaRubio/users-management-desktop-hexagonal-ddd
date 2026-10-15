package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidTeacherAddressException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TeacherAddressTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @ValueSource(strings = {"Calle 123", "   Calle 123   ", "Calle 123 \t"})
    @DisplayName("Should create TeacherAddress with trimmed value")
    void shouldCreateTeacherAddressWithTrimmedValue(final String address) {
        // Arrange
        final String correctAddress = "Calle 123";
        // Act
        final TeacherAddress teacherAddress = new TeacherAddress(address);
        // Assert
        assertEquals(correctAddress, teacherAddress.toString());
    }

    // --- Flujo con excepciones y ramas de validación ---

    @Test
    @DisplayName("Should throw NullPointerException when TeacherAddress is null")
    void shouldThrowNullPointerExceptionWhenTeacherAddressIsNull() {
        assertThrows(NullPointerException.class, () -> new TeacherAddress(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b", "A", "  B  "})
    @DisplayName("Should throw InvalidTeacherAddressException when input is empty or too short")
    void shouldThrowInvalidTeacherAddressExceptionWhenInputIsEmptyOrInvalid(final String address) {
        assertThrows(InvalidTeacherAddressException.class, () -> new TeacherAddress(address));
    }
}