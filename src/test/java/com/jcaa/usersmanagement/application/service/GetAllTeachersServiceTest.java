package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetAllTeachersPort;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para GetAllTeachersService.
 *
 * <p>Cubre: lista con profesores y lista vacía.
 */
@DisplayName("GetAllTeachersService")
@ExtendWith(MockitoExtension.class)
class GetAllTeachersServiceTest {

    @Mock private GetAllTeachersPort getAllTeachersPort;

    private GetAllTeachersService service;

    @BeforeEach
    void setUp() {
        service = new GetAllTeachersService(getAllTeachersPort);
    }

    // ── lista con profesores

    @Test
    @DisplayName("execute() retorna la lista de profesores del puerto")
    void shouldReturnTeachersFromPort() {
        // Arrange
        final TeacherModel teacher =
                new TeacherModel(
                        new TeacherId("t-001"),
                        new TeacherDni("12345678A"),
                        new TeacherName("John Arrieta"),
                        new TeacherAddress("Calle 123"),
                        TeacherStatus.ACTIVE);

        when(getAllTeachersPort.getAll()).thenReturn(List.of(teacher));

        // Act
        final List<TeacherModel> result = service.execute();

        // Assert
        assertAll(
                "getAll con un profesor",
                () -> assertEquals(1, result.size(), "debe retornar exactamente un profesor"),
                () -> assertSame(teacher, result.get(0), "debe ser el mismo objeto del puerto"));
    }

    // ── lista vacía

    @Test
    @DisplayName("execute() retorna lista vacía cuando no hay profesores")
    void shouldReturnEmptyListWhenNoTeachers() {
        // Arrange
        when(getAllTeachersPort.getAll()).thenReturn(List.of());

        // Act
        final List<TeacherModel> result = service.execute();

        // Assert
        assertTrue(result.isEmpty(), "debe retornar lista vacía");
    }
}