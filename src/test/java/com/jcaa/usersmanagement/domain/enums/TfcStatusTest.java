package com.jcaa.usersmanagement.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidTfcStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TfcStatusTest {

    // --- Happy path ---

    @Test
    @DisplayName("Should return correct TfcStatus for valid input")
    void shouldReturnCorrectTfcStatusForValidInput() {
        assertEquals(TfcStatus.PROPOSED, TfcStatus.fromString("PROPOSED"));
        assertEquals(TfcStatus.IN_PROGRESS, TfcStatus.fromString("IN_PROGRESS"));
        assertEquals(TfcStatus.SUBMITTED, TfcStatus.fromString("SUBMITTED"));
        assertEquals(TfcStatus.APPROVED, TfcStatus.fromString("APPROVED"));
        assertEquals(TfcStatus.REJECTED, TfcStatus.fromString("REJECTED"));
    }

    // --- Flujo excepciones y ramas de validación ---

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b", "INVALID_STATUS"})
    @DisplayName("Should throw InvalidTfcStatusException when input is empty or invalid")
    void shouldThrowInvalidTfcStatusExceptionWhenInputIsEmptyOrInvalid(final String input) {
        assertThrows(InvalidTfcStatusException.class, () -> TfcStatus.fromString(input));
    }
}