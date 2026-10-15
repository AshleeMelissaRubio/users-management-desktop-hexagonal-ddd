package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetTfcByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveTfcPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateTfcCommand;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.exception.TfcAlreadyExistsException;
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
 * Tests para CreateTfcService.
 *
 * <p>Cubre: validación del command, ID duplicado, profesor no encontrado y flujo feliz (save).
 */
@DisplayName("CreateTfcService")
@ExtendWith(MockitoExtension.class)
class CreateTfcServiceTest {

    @Mock private SaveTfcPort saveTfcPort;
    @Mock private GetTfcByIdPort getTfcByIdPort;
    @Mock private GetTeacherByIdPort getTeacherByIdPort;

    private CreateTfcService service;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            service =
                    new CreateTfcService(
                            saveTfcPort,
                            getTfcByIdPort,
                            getTeacherByIdPort,
                            validatorFactory.getValidator());
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() guarda el TFC cuando el ID es nuevo y el tutor/profesor existe")
    void shouldSaveTfcWhenIdIsNewAndAdvisorExists() {
        // Arrange
        final CreateTfcCommand command =
                new CreateTfcCommand(
                        "tfc-01", "Sistema de Gestión", "Descripción del proyecto", "t-01");

        final TeacherModel advisor =
                new TeacherModel(
                        new TeacherId("t-01"),
                        new TeacherDni("12345678A"),
                        new TeacherName("Profesor Tutor"),
                        new TeacherAddress("Calle 123"),
                        TeacherStatus.ACTIVE);

        final TfcModel savedTfc =
                new TfcModel(
                        new TfcId("tfc-01"),
                        new TfcTitle("Sistema de Gestión"),
                        new TfcDescription("Descripción del proyecto"),
                        new TeacherId("t-01"),
                        TfcStatus.PROPOSED);

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.empty());
        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.of(advisor));
        when(saveTfcPort.save(any())).thenReturn(savedTfc);

        // Act
        final TfcModel result = service.execute(command);

        // Assert
        assertAll(
                "flujo feliz de CreateTfcService",
                () -> assertNotNull(result, "resultado no debe ser null"),
                () -> assertEquals("tfc-01", result.getId().value(), "id del TFC guardado"),
                () -> assertEquals("PROPOSED", result.getStatus().name(), "estado inicial del TFC"));

        verify(saveTfcPort).save(any(TfcModel.class));
    }

    // ── ID duplicado

    @Test
    @DisplayName("execute() lanza TfcAlreadyExistsException cuando el ID del TFC ya existe")
    void shouldThrowWhenTfcIdAlreadyExists() {
        // Arrange
        final CreateTfcCommand command =
                new CreateTfcCommand("tfc-02", "Plataforma Web", "Descripción web", "t-01");

        final TfcModel existing =
                new TfcModel(
                        new TfcId("tfc-02"),
                        new TfcTitle("Plataforma Web"),
                        new TfcDescription("Descripción web"),
                        new TeacherId("t-01"),
                        TfcStatus.APPROVED);

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.of(existing));

        // Act & Assert
        assertThrows(TfcAlreadyExistsException.class, () -> service.execute(command));
        verify(saveTfcPort, never()).save(any());
    }

    // ── Profesor / Tutor no encontrado

    @Test
    @DisplayName("execute() lanza TeacherNotFoundException cuando el advisorId no existe")
    void shouldThrowWhenAdvisorDoesNotExist() {
        // Arrange
        final CreateTfcCommand command =
                new CreateTfcCommand("tfc-03", "App Móvil", "Descripción app", "t-999");

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.empty());
        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeacherNotFoundException.class, () -> service.execute(command));
        verify(saveTfcPort, never()).save(any());
    }

    // ── validación del command

    @Test
    @DisplayName(
            "execute() lanza ConstraintViolationException cuando el command tiene campos inválidos")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange — campos vacíos / en blanco
        final CreateTfcCommand command =
                new CreateTfcCommand("", "", "", "");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> service.execute(command));
        verifyNoInteractions(saveTfcPort, getTfcByIdPort, getTeacherByIdPort);
    }
}