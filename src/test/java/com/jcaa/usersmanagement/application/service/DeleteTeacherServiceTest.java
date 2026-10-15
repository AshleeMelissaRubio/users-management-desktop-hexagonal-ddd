package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.DeleteTeacherPort;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTeacherCommand;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para DeleteTeacherService.
 *
 * <p>Cubre: flujo feliz (delete invocado), profesor no encontrado y validación del command.
 */
@DisplayName("DeleteTeacherService")
@ExtendWith(MockitoExtension.class)
class DeleteTeacherServiceTest {

    @Mock private DeleteTeacherPort deleteTeacherPort;
    @Mock private GetTeacherByIdPort getTeacherByIdPort;

    private DeleteTeacherService service;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            service =
                    new DeleteTeacherService(
                            deleteTeacherPort, getTeacherByIdPort, validatorFactory.getValidator());
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() invoca deleteTeacherPort cuando el profesor existe")
    void shouldDeleteWhenTeacherExists() {
        // Arrange
        final DeleteTeacherCommand command = new DeleteTeacherCommand("t-001");

        final TeacherModel existing =
                new TeacherModel(
                        new TeacherId("t-001"),
                        new TeacherDni("12345678A"),
                        new TeacherName("John Arrieta"),
                        new TeacherAddress("Calle 123"),
                        TeacherStatus.ACTIVE);

        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.of(existing));

        // Act
        service.execute(command);

        // Assert
        verify(deleteTeacherPort).delete(new TeacherId("t-001"));
    }

    // ── profesor no encontrado

    @Test
    @DisplayName("execute() lanza TeacherNotFoundException cuando el id no existe")
    void shouldThrowWhenTeacherNotFound() {
        // Arrange
        final DeleteTeacherCommand command = new DeleteTeacherCommand("no-existe");

        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeacherNotFoundException.class, () -> service.execute(command));
        verify(deleteTeacherPort, never()).delete(any());
    }

    // ── validación del command

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange
        final DeleteTeacherCommand command = new DeleteTeacherCommand("  ");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> service.execute(command));
        verifyNoInteractions(deleteTeacherPort, getTeacherByIdPort);
    }
}