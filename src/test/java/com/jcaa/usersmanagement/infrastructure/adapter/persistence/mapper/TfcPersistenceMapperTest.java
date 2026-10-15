package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TfcDescription;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.domain.valueobject.TfcTitle;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.TfcPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.TfcEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for TfcPersistenceMapper.
 *
 * <p>Covers: fromModelToDto(), fromEntityToModel(), fromResultSetToEntity() (happy path and
 * SQLException propagation), and fromResultSetToModelList() — empty list, multiple rows, and
 * SQLException propagation during iteration.
 */
@DisplayName("TfcPersistenceMapper")
@ExtendWith(MockitoExtension.class)
class TfcPersistenceMapperTest {

    private static final String ID = "tfc-001";
    private static final String TITLE = "Sistema de Gestión Académica";
    private static final String DESCRIPTION = "Proyecto de fin de grado para arquitectura limpia.";
    private static final String ADVISOR_ID = "t-001";
    private static final String STATUS = "PROPOSED";
    private static final String CREATED_AT = "2024-01-01 00:00:00";
    private static final String UPDATED_AT = "2024-01-02 00:00:00";

    @Mock private ResultSet resultSet;

    private TfcModel tfcModel;
    private TfcEntity tfcEntity;

    @BeforeEach
    void setUp() {
        tfcModel =
                new TfcModel(
                        new TfcId(ID),
                        new TfcTitle(TITLE),
                        new TfcDescription(DESCRIPTION),
                        new TeacherId(ADVISOR_ID),
                        TfcStatus.PROPOSED);

        tfcEntity =
                new TfcEntity(ID, TITLE, DESCRIPTION, ADVISOR_ID, STATUS, CREATED_AT, UPDATED_AT);
    }

    // ── fromModelToDto()

    @Test
    @DisplayName("fromModelToDto() maps all TfcModel fields and sets null timestamps")
    void shouldMapModelToDto() {
        // Act
        final TfcPersistenceDto result = TfcPersistenceMapper.fromModelToDto(tfcModel);

        // Assert
        assertAll(
                "fromModelToDto()",
                () -> assertEquals(ID, result.id(), "id"),
                () -> assertEquals(TITLE, result.title(), "title"),
                () -> assertEquals(DESCRIPTION, result.description(), "description"),
                () -> assertEquals(ADVISOR_ID, result.advisorId(), "advisorId"),
                () -> assertEquals(STATUS, result.status(), "status"),
                () -> assertNull(result.createdAt(), "createdAt must be null"),
                () -> assertNull(result.updatedAt(), "updatedAt must be null"));
    }

    // ── fromEntityToModel()

    @Test
    @DisplayName("fromEntityToModel() maps all TfcEntity fields to a domain TfcModel")
    void shouldMapEntityToModel() {
        // Act
        final TfcModel result = TfcPersistenceMapper.fromEntityToModel(tfcEntity);

        // Assert
        assertAll(
                "fromEntityToModel()",
                () -> assertEquals(ID, result.getId().value(), "id"),
                () -> assertEquals(TITLE, result.getTitle().value(), "title"),
                () -> assertEquals(DESCRIPTION, result.getDescription().value(), "description"),
                () -> assertEquals(ADVISOR_ID, result.getAdvisorId().value(), "advisorId"),
                () -> assertEquals(TfcStatus.PROPOSED, result.getStatus(), "status"));
    }

    // ── fromResultSetToEntity() — happy path

    @Test
    @DisplayName("fromResultSetToEntity() reads all seven columns from the ResultSet")
    void shouldReadAllColumnsFromResultSet() throws SQLException {
        // Arrange
        when(resultSet.getString("id")).thenReturn(ID);
        when(resultSet.getString("title")).thenReturn(TITLE);
        when(resultSet.getString("description")).thenReturn(DESCRIPTION);
        when(resultSet.getString("advisor_id")).thenReturn(ADVISOR_ID);
        when(resultSet.getString("status")).thenReturn(STATUS);
        when(resultSet.getString("created_at")).thenReturn(CREATED_AT);
        when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT);

        // Act
        final TfcEntity result = TfcPersistenceMapper.fromResultSetToEntity(resultSet);

        // Assert
        assertAll(
                "fromResultSetToEntity()",
                () -> assertEquals(ID, result.id(), "id"),
                () -> assertEquals(TITLE, result.title(), "title"),
                () -> assertEquals(DESCRIPTION, result.description(), "description"),
                () -> assertEquals(ADVISOR_ID, result.advisorId(), "advisorId"),
                () -> assertEquals(STATUS, result.status(), "status"),
                () -> assertEquals(CREATED_AT, result.createdAt(), "createdAt"),
                () -> assertEquals(UPDATED_AT, result.updatedAt(), "updatedAt"));
    }

    // ── fromResultSetToEntity() — SQLException propagation

    @Test
    @DisplayName("fromResultSetToEntity() propagates SQLException when ResultSet read fails")
    void shouldPropagateExceptionFromResultSet() throws SQLException {
        // Arrange
        when(resultSet.getString(anyString())).thenThrow(new SQLException("Column read failed"));

        // Act + Assert
        assertThrows(
                SQLException.class,
                () -> TfcPersistenceMapper.fromResultSetToEntity(resultSet),
                "must propagate SQLException when ResultSet throws on getString");
    }

    // ── fromResultSetToModelList() — empty

    @Test
    @DisplayName("fromResultSetToModelList() returns an empty list when ResultSet has no rows")
    void shouldReturnEmptyListWhenResultSetIsEmpty() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(false);

        // Act
        final List<TfcModel> result = TfcPersistenceMapper.fromResultSetToModelList(resultSet);

        // Assert
        assertTrue(result.isEmpty(), "must return an empty list when ResultSet has no rows");
    }

    // ── fromResultSetToModelList() — multiple rows

    @Test
    @DisplayName("fromResultSetToModelList() returns one model per row in the ResultSet")
    void shouldReturnOneModelPerRow() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("id")).thenReturn(ID, "tfc-002");
        when(resultSet.getString("title")).thenReturn(TITLE, "Portal Web para Biblioteca");
        when(resultSet.getString("description")).thenReturn(DESCRIPTION, "Descripción del portal");
        when(resultSet.getString("advisor_id")).thenReturn(ADVISOR_ID, "t-002");
        when(resultSet.getString("status")).thenReturn(STATUS, "APPROVED");
        when(resultSet.getString("created_at")).thenReturn(CREATED_AT, CREATED_AT);
        when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT, UPDATED_AT);

        // Act
        final List<TfcModel> result = TfcPersistenceMapper.fromResultSetToModelList(resultSet);

        // Assert
        assertEquals(2, result.size(), "must return one model per row in the ResultSet");
    }

    // ── fromResultSetToModelList() — SQLException propagation during iteration

    @Test
    @DisplayName("fromResultSetToModelList() propagates SQLException when a row read fails")
    void shouldPropagateExceptionDuringIteration() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(anyString())).thenThrow(new SQLException("Row read failed"));

        // Act + Assert
        assertThrows(
                SQLException.class,
                () -> TfcPersistenceMapper.fromResultSetToModelList(resultSet),
                "must propagate SQLException when a row fails to be read");
    }
}