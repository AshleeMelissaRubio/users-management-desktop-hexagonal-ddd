package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetTfcByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetTfcByIdQuery;
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
 * Tests para GetTfcByIdService.
 *
 * <p>Cubre: retorno del TFC encontrado, TfcNotFoundException y validación del query.
 */
@DisplayName("GetTfcByIdService")
@ExtendWith(MockitoExtension.class)
class GetTfcByIdServiceTest {

    @Mock private GetTfcByIdPort getTfcByIdPort;

    private GetTfcByIdService service;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            service = new GetTfcByIdService(getTfcByIdPort, validatorFactory.getValidator());
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() retorna el TFC cuando el id existe")
    void shouldReturnTfcWhenFound() {
        // Arrange
        final GetTfcByIdQuery query = new GetTfcByIdQuery("tfc-001");

        final TfcModel expected =
                new TfcModel(
                        new TfcId("tfc-001"),
                        new TfcTitle("Sistema de Gestión"),
                        new TfcDescription("Descripción del proyecto"),
                        new TeacherId("t-001"),
                        TfcStatus.PROPOSED);

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.of(expected));

        // Act
        final TfcModel result = service.execute(query);

        // Assert
        assertSame(expected, result, "debe retornar exactamente el TFC del puerto");
    }

    // ── TFC no encontrado

    @Test
    @DisplayName("execute() lanza TfcNotFoundException cuando el id no existe")
    void shouldThrowWhenTfcNotFound() {
        // Arrange
        final GetTfcByIdQuery query = new GetTfcByIdQuery("no-existe");

        when(getTfcByIdPort.getById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TfcNotFoundException.class, () -> service.execute(query));
    }

    // ── validación del query

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
    void shouldThrowWhenQueryIsInvalid() {
        // Arrange
        final GetTfcByIdQuery query = new GetTfcByIdQuery("");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> service.execute(query));
        verifyNoInteractions(getTfcByIdPort);
    }
}