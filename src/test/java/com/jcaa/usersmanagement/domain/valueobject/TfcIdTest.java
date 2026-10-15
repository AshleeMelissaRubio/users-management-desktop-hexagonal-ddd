package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidTfcIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TfcIdTest {

    // --- Happy path ---

    @ParameterizedTest
    @ValueSource(strings = {" tfc-001 ", "  tfc-001  ", "tfc-001\t"})
    @DisplayName("Should create TfcId with trimmed value")
    void shouldCreateTfcIdWithTrimmedValue(final String input) {
        // Arrange
        final String correctTfcId = "tfc-001";
        // Act
        final TfcId tfcId = new TfcId(input);
        // Assert
        assertEquals(correctTfcId, tfcId.toString());
    }

    // --- Flujo con excepciones y ramas condicionales ---

    @Test
    @DisplayName("Should throw NullPointerException when TfcId is null")
    void shouldThrowNullPointerExceptionWhenTfcIdIsNull() {
        assertThrows(NullPointerException.class, () -> new TfcId(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b"})
    @DisplayName("Should throw InvalidTfcIdException when TfcId is empty")
    void shouldThrowInvalidTfcIdExceptionWhenTfcIdIsEmpty(final String input) {
        // Act & Assert
        assertThrows(InvalidTfcIdException.class, () -> new TfcId(input));
    }
}