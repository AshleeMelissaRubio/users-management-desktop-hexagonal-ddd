package com.jcaa.usersmanagement.domain.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TfcNotFoundException.
 *
 * <p>La excepción no tiene lógica de dominio propia; su valor está en el mensaje que produce: el id
 * del TFC no encontrado debe aparecer explícitamente para que la capa superior pueda construir
 * una respuesta de error útil.
 */
@DisplayName("TfcNotFoundException")
class TfcNotFoundExceptionTest {

    @Test
    @DisplayName("becauseIdWasNotFound() debe incluir el id del TFC en el mensaje de error")
    void shouldIncludeTfcIdInMessage() {
        // Arrange
        final String tfcId = "tfc-404";

        // Act
        final String message = TfcNotFoundException.becauseIdWasNotFound(tfcId).getMessage();

        // Assert
        assertTrue(
                message.contains(tfcId), "el mensaje debe identificar el id del TFC no encontrado");
    }
}