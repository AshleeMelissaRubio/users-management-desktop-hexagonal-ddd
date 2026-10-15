package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.DeleteTfcPort;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.port.out.GetTfcByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTfcCommand;
import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
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
 * Tests para DeleteTfcService.
 *
 * <p>Cubre: flujo feliz (delete invocado), TFC no encontrado y validación del command.
 */
@DisplayName("DeleteTfcService")
@ExtendWith(MockitoExtension.class)
class DeleteTfcServiceTest {

    @Mock private DeleteTfcPort deleteTfcPort;
    @Mock private GetTfcByIdPort getTfcByIdPort;
    @Mock private GetTeacherByIdPort getTeacherByIdPort;

    private DeleteTfcService service;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            service =
                    new DeleteTfcService(
                            deleteTfcPort, getTfcByIdPort, getTeacherByIdPort, validatorFactory.getValidator());
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() invoca deleteTfcPort cuando el TFC existe")
    void shouldDeleteWhenTfcExists() {
        // Arrange
        final DeleteTfcCommand command = new DeleteTfcCommand("tfc-001");

        final TfcModel existing =
                new TfcModel(
                        new TfcId("tfc-001"),
                        new TfcTitle("Sistema de Gestión"),
                        new TfcDescription("Descripción del proyecto"),
                        new TeacherId("t-001"),
                        TfcStatus.PROPOSED);

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.of(existing));

        // Act
        service.execute(command);

        // Assert
        verify(deleteTfcPort).delete(new TfcId("tfc-001"));
    }

    // ── TFC no encontrado

    @Test
    @DisplayName("execute() lanza TfcNotFoundException cuando el id no existe")
    void shouldThrowWhenTfcNotFound() {
        // Arrange
        final DeleteTfcCommand command = new DeleteTfcCommand("no-existe");

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TfcNotFoundException.class, () -> service.execute(command));
        verify(deleteTfcPort, never()).delete(any());
    }

    // ── validación del command

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange
        final DeleteTfcCommand command = new DeleteTfcCommand("  ");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> service.execute(command));
        verifyNoInteractions(deleteTfcPort, getTfcByIdPort);
    }
}