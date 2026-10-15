package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetTeacherByDniPort;
import com.jcaa.usersmanagement.application.port.out.SaveTeacherPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateTeacherCommand;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.exception.TeacherAlreadyExistsException;
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
 * Tests para CreateTeacherService.
 *
 * <p>Cubre: validación del command, DNI duplicado y flujo feliz (save).
 */
@DisplayName("CreateTeacherService")
@ExtendWith(MockitoExtension.class)
class CreateTeacherServiceTest {

    @Mock private SaveTeacherPort saveTeacherPort;
    @Mock private GetTeacherByDniPort getTeacherByDniPort;

    private CreateTeacherService service;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            service =
                    new CreateTeacherService(
                            saveTeacherPort,
                            getTeacherByDniPort,
                            validatorFactory.getValidator());
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() guarda el profesor cuando el DNI no existe")
    void shouldSaveTeacherWhenDniIsNew() {
        // Arrange
        final CreateTeacherCommand command =
                new CreateTeacherCommand("t-01", "12345678A", "John Arrieta", "Calle 123");

        final TeacherModel savedTeacher =
                new TeacherModel(
                        new TeacherId("t-01"),
                        new TeacherDni("12345678A"),
                        new TeacherName("John Arrieta"),
                        new TeacherAddress("Calle 123"),
                        TeacherStatus.ACTIVE);

        when(getTeacherByDniPort.getByDni(any())).thenReturn(Optional.empty());
        when(saveTeacherPort.save(any())).thenReturn(savedTeacher);

        // Act
        final TeacherModel result = service.execute(command);

        // Assert
        assertAll(
                "flujo feliz de CreateTeacherService",
                () -> assertNotNull(result, "resultado no debe ser null"),
                () -> assertEquals("t-01", result.getId().value(), "id del profesor guardado"),
                () -> assertEquals("12345678A", result.getDni().value(), "dni del profesor guardado"));

        verify(saveTeacherPort).save(any(TeacherModel.class));
    }

    // ── DNI duplicado

    @Test
    @DisplayName("execute() lanza TeacherAlreadyExistsException cuando el DNI ya está registrado")
    void shouldThrowWhenDniAlreadyExists() {
        // Arrange
        final CreateTeacherCommand command =
                new CreateTeacherCommand("t-02", "87654321B", "Jane Doe", "Avenida 45");

        final TeacherModel existing =
                new TeacherModel(
                        new TeacherId("t-99"),
                        new TeacherDni("87654321B"),
                        new TeacherName("Jane Doe"),
                        new TeacherAddress("Avenida 45"),
                        TeacherStatus.ACTIVE);

        when(getTeacherByDniPort.getByDni(any())).thenReturn(Optional.of(existing));

        // Act & Assert
        assertThrows(TeacherAlreadyExistsException.class, () -> service.execute(command));
        verify(saveTeacherPort, never()).save(any());
    }

    // ── validación del command

    @Test
    @DisplayName(
            "execute() lanza ConstraintViolationException cuando el command tiene campos inválidos")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange — id en blanco y DNI inválido
        final CreateTeacherCommand command =
                new CreateTeacherCommand("", "invalid-dni", "J", "");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> service.execute(command));
        verifyNoInteractions(saveTeacherPort, getTeacherByDniPort);
    }
}