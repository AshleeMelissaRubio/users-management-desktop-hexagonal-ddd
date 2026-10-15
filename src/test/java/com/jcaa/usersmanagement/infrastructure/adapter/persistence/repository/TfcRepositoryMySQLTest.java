package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TfcDescription;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.domain.valueobject.TfcTitle;
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
 * Tests for TfcRepositoryMySQL.
 *
 * <p>Covers all public methods with their branches: save() — happy path, INSERT failure, tfc not
 * found after insert (orElseThrow); update() — happy path, UPDATE failure; getById() — found, not
 * found, SQLException; getAll() — happy path, SQLException; delete() — happy path, SQLException.
 */
@DisplayName("TfcRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
class TfcRepositoryMySQLTest {

    private static final String ID = "tfc-001";
    private static final String TITLE = "Sistema de Gestión Académica";
    private static final String DESCRIPTION = "Proyecto de fin de grado para arquitectura limpia.";
    private static final String ADVISOR_ID = "t-001";
    private static final String STATUS = "PROPOSED";
    private static final String CREATED_AT = "2024-01-01";
    private static final String UPDATED_AT = "2024-01-02";

    @Mock private Connection connection;
    @Mock private PreparedStatement statement;
    @Mock private ResultSet resultSet;

    private TfcRepositoryMySQL repository;
    private TfcModel tfcModel;
    private TfcId tfcId;

    @BeforeEach
    void setUp() {
        repository = new TfcRepositoryMySQL(connection);
        tfcId = new TfcId(ID);
        tfcModel =
                new TfcModel(
                        tfcId,
                        new TfcTitle(TITLE),
                        new TfcDescription(DESCRIPTION),
                        new TeacherId(ADVISOR_ID),
                        TfcStatus.PROPOSED);
    }

    // Helper: wire connection → statement → resultSet
    private void configureStatementAndResultSet() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
    }

    // Helper: configure resultSet to return one full TFC row
    private void configureResultSetRow() throws SQLException {
        when(resultSet.getString("id")).thenReturn(ID);
        when(resultSet.getString("title")).thenReturn(TITLE);
        when(resultSet.getString("description")).thenReturn(DESCRIPTION);
        when(resultSet.getString("advisor_id")).thenReturn(ADVISOR_ID);
        when(resultSet.getString("status")).thenReturn(STATUS);
        when(resultSet.getString("created_at")).thenReturn(CREATED_AT);
        when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT);
    }

    // ── save() — happy path

    @Test
    @DisplayName("save() executes INSERT and returns the persisted TFC fetched by id")
    void shouldSaveTfcAndReturnById() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();

        // Act
        final TfcModel result = repository.save(tfcModel);

        // Assert
        assertAll(
                "save() happy path",
                () -> assertEquals(ID, result.getId().value(), "id"),
                () -> assertEquals(TITLE, result.getTitle().value(), "title"),
                () -> assertEquals(DESCRIPTION, result.getDescription().value(), "description"));
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
                () -> repository.save(tfcModel),
                "must throw PersistenceException when INSERT raises SQLException");
    }

    // ── save() → findByIdOrFail — tfc not found after insert → TfcNotFoundException

    @Test
    @DisplayName("save() throws TfcNotFoundException when the saved TFC cannot be found")
    void shouldThrowTfcNotFoundExceptionWhenTfcNotFoundAfterSave() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(false);

        // Act + Assert
        assertThrows(
                TfcNotFoundException.class,
                () -> repository.save(tfcModel),
                "must throw TfcNotFoundException when SELECT returns no rows after INSERT");
    }

    // ── update() — happy path

    @Test
    @DisplayName("update() executes UPDATE and returns the refreshed TFC fetched by id")
    void shouldUpdateTfcAndReturnById() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();

        // Act
        final TfcModel result = repository.update(tfcModel);

        // Assert
        assertEquals(ID, result.getId().value(), "id must match the updated TFC");
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
                () -> repository.update(tfcModel),
                "must throw PersistenceException when UPDATE raises SQLException");
    }

    // ── getById() — row found → Optional.of(tfc)

    @Test
    @DisplayName("getById() returns Optional.of(tfc) when a matching row exists")
    void shouldReturnTfcWhenFound() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();

        // Act
        final Optional<TfcModel> result = repository.getById(tfcId);

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
        final Optional<TfcModel> result = repository.getById(tfcId);

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
                () -> repository.getById(tfcId),
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
                () -> repository.getById(tfcId),
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
                () -> repository.getById(tfcId),
                "must throw PersistenceException when PreparedStatement.close() raises SQLException after normal body exit");
    }

    // ── getAll() — happy path

    @Test
    @DisplayName("getAll() returns one model per row in the result set")
    void shouldReturnAllTfcs() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true, false);
        configureResultSetRow();

        // Act
        final List<TfcModel> result = repository.getAll();

        // Assert
        assertAll(
                "getAll() happy path",
                () -> assertEquals(1, result.size(), "list size"),
                () -> assertEquals(ID, result.get(0).getId().value(), "first tfc id"));
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
    void shouldDeleteTfcWithoutThrowing() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);

        // Act + Assert
        assertDoesNotThrow(
                () -> repository.delete(tfcId),
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
                () -> repository.delete(tfcId),
                "must throw PersistenceException when DELETE raises SQLException");
    }
}