package com.jcaa.usersmanagement.domain.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TfcAlreadyExistsException.
 *
 * <p>La excepción no tiene lógica de dominio propia; su valor está en el mensaje que produce: el
 * título o identificador duplicado debe aparecer explícitamente para que la capa superior pueda construir una
 * respuesta de error útil.
 */
@DisplayName("TfcAlreadyExistsException")
class TfcAlreadyExistsExceptionTest {

    @Test
    @DisplayName("becauseTitleAlreadyExists() debe incluir el título en el mensaje de error")
    void shouldIncludeTitleInMessage() {
        // Arrange
        final String title = "Sistema de Gestión Duplicado";

        // Act
        final String message = TfcAlreadyExistsException.becauseTitleAlreadyExists(title).getMessage();

        // Assert
        assertTrue(message.contains(title), "el mensaje debe identificar el título duplicado");
    }
}