package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.TeacherPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.TeacherEntity;
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
 * Tests for TeacherPersistenceMapper.
 *
 * <p>Covers: fromModelToDto(), fromEntityToModel(), fromResultSetToEntity() (happy path and
 * SQLException propagation), and fromResultSetToModelList() — empty list, multiple rows, and
 * SQLException propagation during iteration.
 */
@DisplayName("TeacherPersistenceMapper")
@ExtendWith(MockitoExtension.class)
class TeacherPersistenceMapperTest {

    private static final String ID = "t-001";
    private static final String DNI = "12345678A";
    private static final String NAME = "Carlos Pérez";
    private static final String ADDRESS = "Calle 123 #45-67";
    private static final String STATUS = "ACTIVE";
    private static final String CREATED_AT = "2024-01-01 00:00:00";
    private static final String UPDATED_AT = "2024-01-02 00:00:00";

    @Mock private ResultSet resultSet;

    private TeacherModel teacherModel;
    private TeacherEntity teacherEntity;

    @BeforeEach
    void setUp() {
        teacherModel =
                new TeacherModel(
                        new TeacherId(ID),
                        new TeacherDni(DNI),
                        new TeacherName(NAME),
                        new TeacherAddress(ADDRESS),
                        TeacherStatus.ACTIVE);

        teacherEntity = new TeacherEntity(ID, DNI, NAME, ADDRESS, STATUS, CREATED_AT, UPDATED_AT);
    }

    // ── fromModelToDto()

    @Test
    @DisplayName("fromModelToDto() maps all TeacherModel fields and sets null timestamps")
    void shouldMapModelToDto() {
        // Act
        final TeacherPersistenceDto result = TeacherPersistenceMapper.fromModelToDto(teacherModel);

        // Assert
        assertAll(
                "fromModelToDto()",
                () -> assertEquals(ID, result.id(), "id"),
                () -> assertEquals(DNI, result.dni(), "dni"),
                () -> assertEquals(NAME, result.name(), "name"),
                () -> assertEquals(ADDRESS, result.address(), "address"),
                () -> assertEquals(STATUS, result.status(), "status"),
                () -> assertNull(result.createdAt(), "createdAt must be null"),
                () -> assertNull(result.updatedAt(), "updatedAt must be null"));
    }

    // ── fromEntityToModel()

    @Test
    @DisplayName("fromEntityToModel() maps all TeacherEntity fields to a domain TeacherModel")
    void shouldMapEntityToModel() {
        // Act
        final TeacherModel result = TeacherPersistenceMapper.fromEntityToModel(teacherEntity);

        // Assert
        assertAll(
                "fromEntityToModel()",
                () -> assertEquals(ID, result.getId().value(), "id"),
                () -> assertEquals(DNI, result.getDni().value(), "dni"),
                () -> assertEquals(NAME, result.getName().value(), "name"),
                () -> assertEquals(ADDRESS, result.getAddress().value(), "address"),
                () -> assertEquals(TeacherStatus.ACTIVE, result.getStatus(), "status"));
    }

    // ── fromResultSetToEntity() — happy path

    @Test
    @DisplayName("fromResultSetToEntity() reads all seven columns from the ResultSet")
    void shouldReadAllColumnsFromResultSet() throws SQLException {
        // Arrange
        when(resultSet.getString("id")).thenReturn(ID);
        when(resultSet.getString("dni")).thenReturn(DNI);
        when(resultSet.getString("name")).thenReturn(NAME);
        when(resultSet.getString("address")).thenReturn(ADDRESS);
        when(resultSet.getString("status")).thenReturn(STATUS);
        when(resultSet.getString("created_at")).thenReturn(CREATED_AT);
        when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT);

        // Act
        final TeacherEntity result = TeacherPersistenceMapper.fromResultSetToEntity(resultSet);

        // Assert
        assertAll(
                "fromResultSetToEntity()",
                () -> assertEquals(ID, result.id(), "id"),
                () -> assertEquals(DNI, result.dni(), "dni"),
                () -> assertEquals(NAME, result.name(), "name"),
                () -> assertEquals(ADDRESS, result.address(), "address"),
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
                () -> TeacherPersistenceMapper.fromResultSetToEntity(resultSet),
                "must propagate SQLException when ResultSet throws on getString");
    }

    // ── fromResultSetToModelList() — empty

    @Test
    @DisplayName("fromResultSetToModelList() returns an empty list when ResultSet has no rows")
    void shouldReturnEmptyListWhenResultSetIsEmpty() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(false);

        // Act
        final List<TeacherModel> result = TeacherPersistenceMapper.fromResultSetToModelList(resultSet);

        // Assert
        assertTrue(result.isEmpty(), "must return an empty list when ResultSet has no rows");
    }

    // ── fromResultSetToModelList() — multiple rows

    @Test
    @DisplayName("fromResultSetToModelList() returns one model per row in the ResultSet")
    void shouldReturnOneModelPerRow() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("id")).thenReturn(ID, "t-002");
        when(resultSet.getString("dni")).thenReturn(DNI, "87654321B");
        when(resultSet.getString("name")).thenReturn(NAME, "Ana Gomez");
        when(resultSet.getString("address")).thenReturn(ADDRESS, "Carrera 45 #12-34");
        when(resultSet.getString("status")).thenReturn(STATUS, "INACTIVE");
        when(resultSet.getString("created_at")).thenReturn(CREATED_AT, CREATED_AT);
        when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT, UPDATED_AT);

        // Act
        final List<TeacherModel> result = TeacherPersistenceMapper.fromResultSetToModelList(resultSet);

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
                () -> TeacherPersistenceMapper.fromResultSetToModelList(resultSet),
                "must propagate SQLException when a row fails to be read");
    }
}