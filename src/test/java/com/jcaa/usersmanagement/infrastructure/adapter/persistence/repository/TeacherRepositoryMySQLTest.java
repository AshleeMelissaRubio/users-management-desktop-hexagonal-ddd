package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for TeacherRepositoryMySQL.
 *
 * <p>Covers all public methods with their branches: save() — happy path, INSERT failure, teacher not
 * found after insert (orElseThrow); update() — happy path, UPDATE failure; getById() — found, not
 * found, SQLException; getByDni() — found, not found, SQLException; getAll() — happy path,
 * SQLException; delete() — happy path, SQLException.
 */
@DisplayName("TeacherRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
class TeacherRepositoryMySQLTest {

    private static final String ID = "t-001";
    private static final String DNI = "12345678A";
    private static final String NAME = "Carlos Pérez";
    private static final String ADDRESS = "Calle 123 #45-67";
    private static final String STATUS = "ACTIVE";
    private static final String CREATED_AT = "2024-01-01";
    private static final String UPDATED_AT = "2024-01-02";

    @Mock private Connection connection;
    @Mock private PreparedStatement statement;
    @Mock private ResultSet resultSet;

    private TeacherRepositoryMySQL repository;
    private TeacherModel teacherModel;
    private TeacherId teacherId;
    private TeacherDni teacherDni;

    @BeforeEach
    void setUp() {
        repository = new TeacherRepositoryMySQL(connection);
        teacherId = new TeacherId(ID);
        teacherDni = new TeacherDni(DNI);
        teacherModel =
                new TeacherModel(
                        teacherId,
                        teacherDni,
                        new TeacherName(NAME),
                        new TeacherAddress(ADDRESS),
                        TeacherStatus.ACTIVE);
    }

    // Helper: wire connection → statement → resultSet
    private void configureStatementAndResultSet() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
    }

    // Helper: configure resultSet to return one full teacher row
    private void configureResultSetRow() throws SQLException {
        when(resultSet.getString("id")).thenReturn(ID);
        when(resultSet.getString("dni")).thenReturn(DNI);
        when(resultSet.getString("name")).thenReturn(NAME);
        when(resultSet.getString("address")).thenReturn(ADDRESS);
        when(resultSet.getString("status")).thenReturn(STATUS);
        when(resultSet.getString("created_at")).thenReturn(CREATED_AT);
        when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT);
    }

    // ── save() — happy path

    @Test
    @DisplayName("save() executes INSERT and returns the persisted teacher fetched by id")
    void shouldSaveTeacherAndReturnById() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();

        // Act
        final TeacherModel result = repository.save(teacherModel);

        // Assert
        assertAll(
                "save() happy path",
                () -> assertEquals(ID, result.getId().value(), "id"),
                () -> assertEquals(DNI, result.getDni().value(), "dni"),
                () -> assertEquals(NAME, result.getName().value(), "name"));
    }

    // ── save() — INSERT fails → PersistenceException

    @Test
    @DisplayName("save() throws PersistenceException when INSERT raises SQLException")
    void shouldThrowPersistenceExceptionWhenInsertFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException("Insert failed"));

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.save(teacherModel),
                "must throw PersistenceException when INSERT raises SQLException");
    }

    // ── save() → findByIdOrFail — teacher not found after insert → TeacherNotFoundException

    @Test
    @DisplayName("save() throws TeacherNotFoundException when the saved teacher cannot be found")
    void shouldThrowTeacherNotFoundExceptionWhenTeacherNotFoundAfterSave() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(false);

        // Act + Assert
        assertThrows(
                TeacherNotFoundException.class,
                () -> repository.save(teacherModel),
                "must throw TeacherNotFoundException when SELECT returns no rows after INSERT");
    }

    // ── update() — happy path

    @Test
    @DisplayName("update() executes UPDATE and returns the refreshed teacher fetched by id")
    void shouldUpdateTeacherAndReturnById() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();

        // Act
        final TeacherModel result = repository.update(teacherModel);

        // Assert
        assertEquals(ID, result.getId().value(), "id must match the updated teacher");
    }

    // ── update() — UPDATE fails → PersistenceException

    @Test
    @DisplayName("update() throws PersistenceException when UPDATE raises SQLException")
    void shouldThrowPersistenceExceptionWhenUpdateFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException("Update failed"));

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.update(teacherModel),
                "must throw PersistenceException when UPDATE raises SQLException");
    }

    // ── getById() — row found → Optional.of(teacher)

    @Test
    @DisplayName("getById() returns Optional.of(teacher) when a matching row exists")
    void shouldReturnTeacherWhenFound() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();

        // Act
        final Optional<TeacherModel> result = repository.getById(teacherId);

        // Assert
        assertAll(
                "getById() found",
                () -> assertTrue(result.isPresent(), "must be present"),
                () -> assertEquals(ID, result.get().getId().value(), "id"));
    }

    // ── getById() — no row → Optional.empty()

    @Test
    @DisplayName("getById() returns Optional.empty() when no matching row exists")
    void shouldReturnEmptyWhenNotFound() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(false);

        // Act
        final Optional<TeacherModel> result = repository.getById(teacherId);

        // Assert
        assertTrue(result.isEmpty(), "must return Optional.empty() when no row matches the id");
    }

    // ── getById() — SQLException → PersistenceException (from prepareStatement)

    @Test
    @DisplayName("getById() throws PersistenceException when prepareStatement raises SQLException")
    void shouldThrowPersistenceExceptionOnGetByIdFailure() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.getById(teacherId),
                "must throw PersistenceException when prepareStatement raises SQLException");
    }

    // ── getById() — SQLException → PersistenceException (from executeQuery, inside try body)

    @Test
    @DisplayName("getById() throws PersistenceException when executeQuery raises SQLException")
    void shouldThrowPersistenceExceptionWhenGetByIdExecuteQueryFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException("Execute query failed"));

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.getById(teacherId),
                "must throw PersistenceException when executeQuery raises SQLException inside the try block");
    }

    // ── getById() — SQLException → PersistenceException (from statement.close() after normal exit)

    @Test
    @DisplayName(
            "getById() throws PersistenceException when PreparedStatement.close() raises SQLException")
    void shouldThrowPersistenceExceptionWhenGetByIdStatementCloseFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        doThrow(new SQLException("Close failed")).when(statement).close();

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.getById(teacherId),
                "must throw PersistenceException when PreparedStatement.close() raises SQLException after normal body exit");
    }

    // ── getByDni() — row found → Optional.of(teacher)

    @Test
    @DisplayName("getByDni() returns Optional.of(teacher) when a matching row exists")
    void shouldReturnTeacherByDniWhenFound() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();

        // Act
        final Optional<TeacherModel> result = repository.getByDni(teacherDni);

        // Assert
        assertAll(
                "getByDni() found",
                () -> assertTrue(result.isPresent(), "must be present"),
                () -> assertEquals(DNI, result.get().getDni().value(), "dni"));
    }

    // ── getByDni() — no row → Optional.empty()

    @Test
    @DisplayName("getByDni() returns Optional.empty() when no matching row exists")
    void shouldReturnEmptyWhenDniNotFound() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(false);

        // Act
        final Optional<TeacherModel> result = repository.getByDni(teacherDni);

        // Assert
        assertTrue(result.isEmpty(), "must return Optional.empty() when no row matches the dni");
    }

    // ── getByDni() — SQLException → PersistenceException (from prepareStatement)

    @Test
    @DisplayName("getByDni() throws PersistenceException when prepareStatement raises SQLException")
    void shouldThrowPersistenceExceptionOnGetByDniFailure() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.getByDni(teacherDni),
                "must throw PersistenceException when prepareStatement raises SQLException");
    }

    // ── getByDni() — SQLException → PersistenceException (from executeQuery, inside try body)

    @Test
    @DisplayName("getByDni() throws PersistenceException when executeQuery raises SQLException")
    void shouldThrowPersistenceExceptionWhenGetByDniExecuteQueryFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException("Execute query failed"));

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.getByDni(teacherDni),
                "must throw PersistenceException when executeQuery raises SQLException inside the try block");
    }

    // ── getByDni() — SQLException → PersistenceException (from statement.close() after normal exit)

    @Test
    @DisplayName(
            "getByDni() throws PersistenceException when PreparedStatement.close() raises SQLException")
    void shouldThrowPersistenceExceptionWhenGetByDniStatementCloseFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        doThrow(new SQLException("Close failed")).when(statement).close();

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.getByDni(teacherDni),
                "must throw PersistenceException when PreparedStatement.close() raises SQLException after normal body exit");
    }

    // ── getAll() — happy path

    @Test
    @DisplayName("getAll() returns one model per row in the result set")
    void shouldReturnAllTeachers() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true, false);
        configureResultSetRow();

        // Act
        final List<TeacherModel> result = repository.getAll();

        // Assert
        assertAll(
                "getAll() happy path",
                () -> assertEquals(1, result.size(), "list size"),
                () -> assertEquals(ID, result.get(0).getId().value(), "first teacher id"));
    }

    // ── getAll() — SQLException → PersistenceException

    @Test
    @DisplayName("getAll() throws PersistenceException when the query raises SQLException")
    void shouldThrowPersistenceExceptionOnGetAllFailure() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.getAll(),
                "must throw PersistenceException when SELECT raises SQLException");
    }

    // ── delete() — happy path

    @Test
    @DisplayName("delete() executes DELETE without throwing")
    void shouldDeleteTeacherWithoutThrowing() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        // Act + Assert
        assertDoesNotThrow(
                () -> repository.delete(teacherId),
                "delete() must not throw when DELETE executes successfully");
    }

    // ── delete() — SQLException → PersistenceException

    @Test
    @DisplayName("delete() throws PersistenceException when DELETE raises SQLException")
    void shouldThrowPersistenceExceptionWhenDeleteFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> repository.delete(teacherId),
                "must throw PersistenceException when DELETE raises SQLException");
    }
}