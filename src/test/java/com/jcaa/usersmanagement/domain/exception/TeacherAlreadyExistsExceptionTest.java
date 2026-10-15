package com.jcaa.usersmanagement.domain.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TeacherAlreadyExistsException.
 *
 * <p>La excepción no tiene lógica de dominio propia; su valor está en el mensaje que produce: el
 * DNI duplicado debe aparecer explícitamente para que la capa superior pueda construir una
 * respuesta de error útil.
 */
@DisplayName("TeacherAlreadyExistsException")
class TeacherAlreadyExistsExceptionTest {

    @Test
    @DisplayName("becauseDniAlreadyExists() debe incluir el DNI en el mensaje de error")
    void shouldIncludeDniInMessage() {
        // Arrange
        final String dni = "12345678A";

        // Act
        final String message = TeacherAlreadyExistsException.becauseDniAlreadyExists(dni).getMessage();

        // Assert
        assertTrue(message.contains(dni), "el mensaje debe identificar el DNI duplicado");
    }
}