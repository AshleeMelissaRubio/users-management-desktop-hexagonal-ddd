package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidTfcTitleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TfcTitleTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @ValueSource(strings = {"Sistema de Gestión", "   Sistema de Gestión   ", "Sistema de Gestión \t"})
    @DisplayName("Valida que el título sea correcto y aplique trim")
    void shouldValidateTfcTitleMinimumLength(final String title) {
        // Arrange
        final String correctTitle = "Sistema de Gestión";
        // Act
        final TfcTitle tfcTitleVo = new TfcTitle(title);
        // Assert
        assertEquals(correctTitle, tfcTitleVo.toString());
    }

    // -- Flujo con excepciones y ramas de validación ---

    @Test
    @DisplayName("Valida que el título no sea nulo")
    void shouldValidateTfcTitleIsNotNull() {
        assertThrows(NullPointerException.class, () -> new TfcTitle(null));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"", "  ", "\t", "\n", "\r", "\f", "\b", "Ab", "T   ", "   Cy ", "Ed\t"})
    @DisplayName("Valida que el título no sea vacío y tenga la longitud mínima requerida")
    void shouldValidateTfcTitleIsNotEmptyAndMinimumLength(final String title) {
        assertThrows(InvalidTfcTitleException.class, () -> new TfcTitle(title));
    }
}