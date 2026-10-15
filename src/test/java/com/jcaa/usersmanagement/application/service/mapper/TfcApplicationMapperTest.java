package com.jcaa.usersmanagement.application.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.application.service.dto.command.CreateTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetTfcByIdQuery;
import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TfcApplicationMapper.
 *
 * <p>Cubre cada método público de conversión mapeando comandos y queries de TFC a modelos de
 * dominio (fijando status inicial PENDING) y manteniendo el estado actual al actualizar.
 */
@DisplayName("TfcApplicationMapper")
class TfcApplicationMapperTest {

    private static final String ID = "tfc-001";
    private static final String TITLE = "Sistema de Gestión Académica";
    private static final String DESCRIPTION = "Proyecto de fin de grado para arquitectura limpia.";
    private static final String ADVISOR_ID = "t-001";
    private static final String STATUS   = "PROPOSED";

    // ── fromCreateCommandToModel()

    @Test
    @DisplayName("fromCreateCommandToModel() mapea todos los campos y fija el estado inicial PENDING")
    void shouldMapCreateCommandToModelWithPendingStatus() {
        // Arrange
        final CreateTfcCommand command = new CreateTfcCommand(ID, TITLE, DESCRIPTION, ADVISOR_ID);

        // Act
        final TfcModel result = TfcApplicationMapper.fromCreateCommandToModel(command);

        // Assert
        assertAll(
                "fromCreateCommandToModel()",
                () -> assertEquals(ID, result.getId().value(), "id"),
                () -> assertEquals(TITLE, result.getTitle().value(), "title"),
                () -> assertEquals(DESCRIPTION, result.getDescription().value(), "description"),
                () -> assertEquals(ADVISOR_ID, result.getAdvisorId().value(), "advisorId"),
                () -> assertEquals(TfcStatus.PROPOSED, result.getStatus(), "status debe ser PROPOSED"));
    }

    // ── fromUpdateCommandToModel()

    @Test
    @DisplayName("fromUpdateCommandToModel() conserva el estado del TFC que recibe por parámetro")
    void shouldMapUpdateCommandToModelPreservingStatus() {
        // Arrange
        final TfcStatus currentStatus = TfcStatus.APPROVED;
        final UpdateTfcCommand command = new UpdateTfcCommand(ID, TITLE, DESCRIPTION, ADVISOR_ID, STATUS);

        // Act
        final TfcModel result = TfcApplicationMapper.fromUpdateCommandToModel(command, currentStatus);

        // Assert
        assertAll(
                "fromUpdateCommandToModel()",
                () -> assertEquals(ID, result.getId().value(), "id"),
                () -> assertEquals(TITLE, result.getTitle().value(), "title"),
                () -> assertEquals(DESCRIPTION, result.getDescription().value(), "description"),
                () -> assertEquals(ADVISOR_ID, result.getAdvisorId().value(), "advisorId"),
                () -> assertEquals(TfcStatus.APPROVED, result.getStatus(), "status debe conservar el estado actual"));
    }

    // ── fromGetTfcByIdQueryToTfcId()

    @Test
    @DisplayName("fromGetTfcByIdQueryToTfcId() extrae el TfcId del query")
    void shouldExtractTfcIdFromQuery() {
        // Arrange
        final GetTfcByIdQuery query = new GetTfcByIdQuery(ID);

        // Act
        final TfcId result = TfcApplicationMapper.fromGetTfcByIdQueryToTfcId(query);

        // Assert
        assertEquals(ID, result.value(), "id debe coincidir con el del query");
    }

    // ── fromDeleteCommandToTfcId()

    @Test
    @DisplayName("fromDeleteCommandToTfcId() extrae el TfcId del command")
    void shouldExtractTfcIdFromDeleteCommand() {
        // Arrange
        final DeleteTfcCommand command = new DeleteTfcCommand(ID);

        // Act
        final TfcId result = TfcApplicationMapper.fromDeleteCommandToTfcId(command);

        // Assert
        assertEquals(ID, result.value(), "id debe coincidir con el del command");
    }
}