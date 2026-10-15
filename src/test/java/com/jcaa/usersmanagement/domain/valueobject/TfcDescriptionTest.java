package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidTfcDescriptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TfcDescriptionTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @ValueSource(
            strings = {
                    "Descripción detallada del proyecto",
                    "   Descripción detallada del proyecto   ",
                    "Descripción detallada del proyecto \t"
            })
    @DisplayName("Should create TfcDescription with trimmed value when valid")
    void shouldCreateTfcDescriptionWithTrimmedValue(final String description) {
        // Arrange
        final String correctDescription = "Descripción detallada del proyecto";
        // Act
        final TfcDescription tfcDescription = new TfcDescription(description);
        // Assert
        assertEquals(correctDescription, tfcDescription.toString());
    }

    // --- Flujo con excepciones y ramas de validación ---

    @Test
    @DisplayName("Should throw NullPointerException when TfcDescription is null")
    void shouldThrowNullPointerExceptionWhenTfcDescriptionIsNull() {
        assertThrows(NullPointerException.class, () -> new TfcDescription(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b", "Corta", "   1234   "})
    @DisplayName("Should throw InvalidTfcDescriptionException when input is empty or too short")
    void shouldThrowInvalidTfcDescriptionExceptionWhenInputIsEmptyOrInvalid(final String description) {
        assertThrows(InvalidTfcDescriptionException.class, () -> new TfcDescription(description));
    }
}