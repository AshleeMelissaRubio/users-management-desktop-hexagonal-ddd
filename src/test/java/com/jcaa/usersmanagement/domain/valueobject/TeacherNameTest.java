package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidTeacherNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TeacherNameTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @ValueSource(strings = {"John Arrieta", "   John Arrieta   ", "John Arrieta \t"})
    @DisplayName("Valida que el nombre tenga al menos 3 caracteres y aplique trim")
    void shouldValidateTeacherNameMinimumLength(final String teacherName) {
        // Arrange
        final String correctTeacherName = "John Arrieta";
        // Act
        final TeacherName teacherNameVo = new TeacherName(teacherName);
        // Assert
        assertEquals(correctTeacherName, teacherNameVo.toString());
    }

    // -- Flujo con excepciones y ramas de validación ---

    @Test
    @DisplayName("Valida que el nombre no sea nulo")
    void shouldValidateTeacherNameIsNotNull() {
        assertThrows(NullPointerException.class, () -> new TeacherName(null));
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"", "  ", "\t", "\n", "\r", "\f", "\b", "Jo", "Ty  ", "   Cy ", "Ed\t"})
    @DisplayName("Valida que el nombre no sea vacio y tenga un tamaño minimo")
    void shouldValidateTeacherNameIsNotEmptyAndMinimumLength(final String teacherName) {
        assertThrows(InvalidTeacherNameException.class, () -> new TeacherName(teacherName));
    }
}