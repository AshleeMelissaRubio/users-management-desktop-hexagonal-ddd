package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TfcDescription;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.domain.valueobject.TfcTitle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TfcModel.
 *
 * <p>Sólo se testean los métodos con lógica real: {@code create()} que fija el estado inicial
 * PROPOSED y las transiciones de estado (e.g., {@code approve()} y {@code reject()}). El
 * constructor trivial y los getters quedan cubiertos como efecto secundario.
 */
@DisplayName("TfcModel")
class TfcModelTest {

    // ── Arrange (variables globales para la mayoría de las test)
    private TfcId tfcId;
    private TfcTitle tfcTitle;
    private TfcDescription tfcDescription;
    private TeacherId advisorId;

    @BeforeEach
    void setUp() {
        tfcId = new TfcId("tfc-001");
        tfcTitle = new TfcTitle("Sistema de Gestión");
        tfcDescription = new TfcDescription("Descripción del proyecto");
        advisorId = new TeacherId("t-001");
    }

    // ── create()

    @Test
    @DisplayName("create() debe fijar status PROPOSED y preservar todos los campos recibidos")
    void shouldCreateTfcWithProposedStatusAndPreserveAllFields() {
        // Act
        final TfcModel model =
                TfcModel.create(tfcId, tfcTitle, tfcDescription, advisorId);

        // Assert
        assertAll(
                "create() factory",
                () ->
                        assertEquals(
                                TfcStatus.PROPOSED, model.getStatus(), "status debe iniciar como PROPOSED"),
                () -> assertSame(tfcId, model.getId(), "id debe preservarse"),
                () -> assertSame(tfcTitle, model.getTitle(), "title debe preservarse"),
                () -> assertSame(tfcDescription, model.getDescription(), "description debe preservarse"),
                () -> assertSame(advisorId, model.getAdvisorId(), "advisorId debe preservarse"));
    }

    // ── approve()

    @Test
    @DisplayName("approve() debe retornar nueva instancia con APPROVED y demás campos intactos")
    void shouldApproveAndPreserveOtherFields() {
        // Arrange
        final TfcModel proposed =
                TfcModel.create(tfcId, tfcTitle, tfcDescription, advisorId);

        // Act
        final TfcModel approved = proposed.approve();

        // Assert
        assertAll(
                "resultado de approve()",
                () -> assertNotSame(proposed, approved, "debe ser una nueva instancia"),
                () -> assertEquals(TfcStatus.APPROVED, approved.getStatus(), "status debe ser APPROVED"),
                () -> assertSame(tfcId, approved.getId(), "id debe preservarse"),
                () -> assertSame(tfcTitle, approved.getTitle(), "title debe preservarse"),
                () -> assertSame(tfcDescription, approved.getDescription(), "description debe preservarse"),
                () -> assertSame(advisorId, approved.getAdvisorId(), "advisorId debe preservarse"));
    }

    // ── reject()

    @Test
    @DisplayName("reject() debe retornar nueva instancia con REJECTED y demás campos intactos")
    void shouldRejectAndPreserveOtherFields() {
        // Arrange
        final TfcModel proposed =
                TfcModel.create(tfcId, tfcTitle, tfcDescription, advisorId);

        // Act
        final TfcModel rejected = proposed.reject();

        // Assert
        assertAll(
                "resultado de reject()",
                () -> assertNotSame(proposed, rejected, "debe ser una nueva instancia"),
                () -> assertEquals(TfcStatus.REJECTED, rejected.getStatus(), "status debe ser REJECTED"),
                () -> assertSame(tfcId, rejected.getId(), "id debe preservarse"),
                () -> assertSame(tfcTitle, rejected.getTitle(), "title debe preservarse"),
                () -> assertSame(tfcDescription, rejected.getDescription(), "description debe preservarse"),
                () -> assertSame(advisorId, rejected.getAdvisorId(), "advisorId debe preservarse"));
    }
}