package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetTeacherByDniPort;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateTeacherPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTeacherCommand;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.exception.TeacherAlreadyExistsException;
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
 * Tests para UpdateTeacherService.
 *
 * <p>Cubre: flujo feliz, profesor no encontrado, DNI tomado por otro profesor, DNI del mismo
 * profesor (no debe fallar) y validación del command.
 */
@DisplayName("UpdateTeacherService")
@ExtendWith(MockitoExtension.class)
class UpdateTeacherServiceTest {

    @Mock private UpdateTeacherPort updateTeacherPort;
    @Mock private GetTeacherByIdPort getTeacherByIdPort;
    @Mock private GetTeacherByDniPort getTeacherByDniPort;

    private UpdateTeacherService service;

    private static final String ID = "t-001";
    private static final String DNI = "12345678A";

    private TeacherModel existingTeacher;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            service =
                    new UpdateTeacherService(
                            updateTeacherPort,
                            getTeacherByIdPort,
                            getTeacherByDniPort,
                            validatorFactory.getValidator());
        }

        existingTeacher =
                new TeacherModel(
                        new TeacherId(ID),
                        new TeacherDni(DNI),
                        new TeacherName("John Arrieta"),
                        new TeacherAddress("Calle 123"),
                        TeacherStatus.ACTIVE);
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() actualiza el profesor cuando los datos son válidos")
    void shouldUpdateTeacherWhenDataIsValid() {
        // Arrange
        final UpdateTeacherCommand command =
                new UpdateTeacherCommand(ID, DNI, "John Updated", "Calle 456", "ACTIVE");

        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.of(existingTeacher));
        when(getTeacherByDniPort.getByDni(any())).thenReturn(Optional.of(existingTeacher));
        when(updateTeacherPort.update(any())).thenReturn(existingTeacher);

        // Act
        final TeacherModel result = service.execute(command);

        // Assert
        assertNotNull(result);
        verify(updateTeacherPort).update(any(TeacherModel.class));
    }

    // ── profesor no encontrado

    @Test
    @DisplayName("execute() lanza TeacherNotFoundException cuando el id no existe")
    void shouldThrowWhenTeacherNotFound() {
        // Arrange
        final UpdateTeacherCommand command =
                new UpdateTeacherCommand("no-existe", DNI, "Name", "Calle 123", "ACTIVE");

        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeacherNotFoundException.class, () -> service.execute(command));
        verify(updateTeacherPort, never()).update(any());
    }

    // ── DNI tomado por otro profesor

    @Test
    @DisplayName(
            "execute() lanza TeacherAlreadyExistsException cuando el DNI pertenece a otro profesor")
    void shouldThrowWhenDniBelongsToAnotherTeacher() {
        // Arrange
        final UpdateTeacherCommand command =
                new UpdateTeacherCommand(ID, "87654321B", "John", "Calle 123", "ACTIVE");

        final TeacherModel otherTeacher =
                new TeacherModel(
                        new TeacherId("t-999"),
                        new TeacherDni("87654321B"),
                        new TeacherName("Other Teacher"),
                        new TeacherAddress("Avenida 45"),
                        TeacherStatus.ACTIVE);

        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.of(existingTeacher));
        when(getTeacherByDniPort.getByDni(any())).thenReturn(Optional.of(otherTeacher));

        // Act & Assert
        assertThrows(TeacherAlreadyExistsException.class, () -> service.execute(command));
        verify(updateTeacherPort, never()).update(any());
    }

    // ── DNI del mismo profesor: no debe lanzar excepción

    @Test
    @DisplayName("execute() permite mantener el mismo DNI del propio profesor")
    void shouldAllowKeepingOwnDni() {
        // Arrange
        final UpdateTeacherCommand command =
                new UpdateTeacherCommand(ID, DNI, "John Updated", "Calle 123", "ACTIVE");

        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.of(existingTeacher));
        when(getTeacherByDniPort.getByDni(any())).thenReturn(Optional.of(existingTeacher));
        when(updateTeacherPort.update(any())).thenReturn(existingTeacher);

        // Act & Assert
        assertDoesNotThrow(() -> service.execute(command));
        verify(updateTeacherPort).update(any());
    }

    // ── validación del command

    @Test
    @DisplayName(
            "execute() lanza ConstraintViolationException cuando el command tiene campos inválidos")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange — id en blanco y DNI inválido
        final UpdateTeacherCommand command =
                new UpdateTeacherCommand("", "invalid-dni", "J", "", "ACTIVE");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> service.execute(command));
        verifyNoInteractions(updateTeacherPort, getTeacherByIdPort, getTeacherByDniPort);
    }
}