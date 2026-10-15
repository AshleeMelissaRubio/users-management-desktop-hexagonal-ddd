package com.jcaa.usersmanagement.domain.exception;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TeacherNotFoundException.
 *
 * <p>La excepción no tiene lógica de dominio propia; su valor está en el mensaje que produce: el id
 * del profesor no encontrado debe aparecer explícitamente para que la capa superior pueda construir
 * una respuesta de error útil.
 */
@DisplayName("TeacherNotFoundException")
class TeacherNotFoundExceptionTest {

    @Test
    @DisplayName("becauseIdWasNotFound() debe incluir el id del profesor en el mensaje de error")
    void shouldIncludeTeacherIdInMessage() {
        // Arrange
        final String teacherId = "teacher-404";

        // Act
        final String message = TeacherNotFoundException.becauseIdWasNotFound(teacherId).getMessage();

        // Assert
        assertTrue(
                message.contains(teacherId), "el mensaje debe identificar el id del profesor no encontrado");
    }
}