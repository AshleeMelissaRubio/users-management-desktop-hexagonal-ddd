package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetAllTfcsPort;
import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TfcDescription;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.domain.valueobject.TfcTitle;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para GetAllTfcsService.
 *
 * <p>Cubre: lista con TFCs y lista vacía.
 */
@DisplayName("GetAllTfcsService")
@ExtendWith(MockitoExtension.class)
class GetAllTfcsServiceTest {

    @Mock private GetAllTfcsPort getAllTfcsPort;

    private GetAllTfcsService service;

    @BeforeEach
    void setUp() {
        service = new GetAllTfcsService(getAllTfcsPort);
    }

    // ── lista con TFCs

    @Test
    @DisplayName("execute() retorna la lista de TFCs del puerto")
    void shouldReturnTfcsFromPort() {
        // Arrange
        final TfcModel tfc =
                new TfcModel(
                        new TfcId("tfc-001"),
                        new TfcTitle("Sistema de Gestión"),
                        new TfcDescription("Descripción del proyecto"),
                        new TeacherId("t-001"),
                        TfcStatus.PROPOSED);

        when(getAllTfcsPort.getAll()).thenReturn(List.of(tfc));

        // Act
        final List<TfcModel> result = service.execute();

        // Assert
        assertAll(
                "getAll con un TFC",
                () -> assertEquals(1, result.size(), "debe retornar exactamente un TFC"),
                () -> assertSame(tfc, result.get(0), "debe ser el mismo objeto del puerto"));
    }

    // ── lista vacía

    @Test
    @DisplayName("execute() retorna lista vacía cuando no hay TFCs")
    void shouldReturnEmptyListWhenNoTfcs() {
        // Arrange
        when(getAllTfcsPort.getAll()).thenReturn(List.of());

        // Act
        final List<TfcModel> result = service.execute();

        // Assert
        assertTrue(result.isEmpty(), "debe retornar lista vacía");
    }
}