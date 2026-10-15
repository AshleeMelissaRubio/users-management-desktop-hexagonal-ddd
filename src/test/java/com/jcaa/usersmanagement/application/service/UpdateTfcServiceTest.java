package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetTfcByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateTfcPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTfcCommand;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import com.jcaa.usersmanagement.domain.valueobject.TfcDescription;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.domain.valueobject.TfcTitle;
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
 * Tests para UpdateTfcService.
 *
 * <p>Cubre: flujo feliz, TFC no encontrado, profesor/tutor asignado no encontrado y validación del command.
 */
@DisplayName("UpdateTfcService")
@ExtendWith(MockitoExtension.class)
class UpdateTfcServiceTest {

    @Mock private UpdateTfcPort updateTfcPort;
    @Mock private GetTfcByIdPort getTfcByIdPort;
    @Mock private GetTeacherByIdPort getTeacherByIdPort;

    private UpdateTfcService service;

    private static final String TFC_ID = "tfc-001";
    private static final String TEACHER_ID = "t-001";

    private TfcModel existingTfc;
    private TeacherModel existingTeacher;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            service =
                    new UpdateTfcService(
                            updateTfcPort,
                            getTfcByIdPort,
                            getTeacherByIdPort,
                            validatorFactory.getValidator());
        }

        existingTfc =
                new TfcModel(
                        new TfcId(TFC_ID),
                        new TfcTitle("Sistema de Gestión"),
                        new TfcDescription("Descripción del proyecto"),
                        new TeacherId(TEACHER_ID),
                        TfcStatus.PROPOSED);

        existingTeacher =
                new TeacherModel(
                        new TeacherId(TEACHER_ID),
                        new TeacherDni("12345678A"),
                        new TeacherName("John Arrieta"),
                        new TeacherAddress("Calle 123"),
                        TeacherStatus.ACTIVE);
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() actualiza el TFC cuando los datos son válidos y el profesor existe")
    void shouldUpdateTfcWhenDataIsValidAndTeacherExists() {
        // Arrange
        final UpdateTfcCommand command =
                new UpdateTfcCommand(
                        TFC_ID, "Sistema de Gestión V2", "Nueva descripción", TEACHER_ID, "APPROVED");

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.of(existingTfc));
        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.of(existingTeacher));
        when(updateTfcPort.update(any())).thenReturn(existingTfc);

        // Act
        final TfcModel result = service.execute(command);

        // Assert
        assertNotNull(result);
        verify(updateTfcPort).update(any(TfcModel.class));
    }

    // ── TFC no encontrado

    @Test
    @DisplayName("execute() lanza TfcNotFoundException cuando el id del TFC no existe")
    void shouldThrowWhenTfcNotFound() {
        // Arrange
        final UpdateTfcCommand command =
                new UpdateTfcCommand(
                        "no-existe", "Título", "Descripción", TEACHER_ID, "PROPOSED");

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TfcNotFoundException.class, () -> service.execute(command));
        verify(updateTfcPort, never()).update(any());
    }

    // ── Profesor / Tutor no encontrado

    @Test
    @DisplayName("execute() lanza TeacherNotFoundException cuando el advisorId no existe")
    void shouldThrowWhenAdvisorNotFound() {
        // Arrange
        final UpdateTfcCommand command =
                new UpdateTfcCommand(
                        TFC_ID, "Título", "Descripción", "t-999", "PROPOSED");

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.of(existingTfc));
        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeacherNotFoundException.class, () -> service.execute(command));
        verify(updateTfcPort, never()).update(any());
    }

    // ── validación del command

    @Test
    @DisplayName(
            "execute() lanza ConstraintViolationException cuando el command tiene campos inválidos")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange — campos vacíos / en blanco
        final UpdateTfcCommand command =
                new UpdateTfcCommand("", "", "", "", "");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> service.execute(command));
        verifyNoInteractions(updateTfcPort, getTfcByIdPort, getTeacherByIdPort);
    }
}