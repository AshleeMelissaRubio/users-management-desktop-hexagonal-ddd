package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteTfcPort;
import com.jcaa.usersmanagement.application.port.out.GetAllTfcsPort;
import com.jcaa.usersmanagement.application.port.out.GetTfcByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveTfcPort;
import com.jcaa.usersmanagement.application.port.out.UpdateTfcPort;
import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.TfcPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.TfcPersistenceMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public final class TfcRepositoryMySQL
        implements SaveTfcPort,
        UpdateTfcPort,
        GetTfcByIdPort,
        GetAllTfcsPort,
        DeleteTfcPort {

    private static final String SQL_INSERT =
            "INSERT INTO tfcs "
                    + "(id, title, description, advisor_id, status, created_at, updated_at) "
                    + "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";

    private static final String SQL_UPDATE =
            "UPDATE tfcs SET title = ?, description = ?, advisor_id = ?, status = ?, updated_at = NOW() "
                    + "WHERE id = ?";

    private static final String SQL_SELECT_BY_ID =
            "SELECT id, title, description, advisor_id, status, created_at, updated_at "
                    + "FROM tfcs "
                    + "WHERE id = ? LIMIT 1";

    private static final String SQL_SELECT_ALL =
            "SELECT id, title, description, advisor_id, status, created_at, updated_at "
                    + "FROM tfcs "
                    + "ORDER BY title ASC";

    private static final String SQL_DELETE =
            "DELETE FROM tfcs "
                    + "WHERE id = ?";

    private final Connection connection;

    @Override
    public TfcModel save(final TfcModel tfc) {
        final TfcPersistenceDto dto = TfcPersistenceMapper.fromModelToDto(tfc);
        executeSave(dto);
        return findByIdOrFail(tfc.getId());
    }

    @Override
    public TfcModel update(final TfcModel tfc) {
        final TfcPersistenceDto dto = TfcPersistenceMapper.fromModelToDto(tfc);
        executeUpdate(dto);
        return findByIdOrFail(tfc.getId());
    }

    @Override
    public Optional<TfcModel> getById(final TfcId tfcId) {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setString(1, tfcId.value());
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(TfcPersistenceMapper.fromResultSetToModel(resultSet));
        } catch (final SQLException exception) {
            throw PersistenceException.becauseFindByIdFailed(tfcId.value(), exception);
        }
    }

    @Override
    public List<TfcModel> getAll() {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
            final ResultSet resultSet = statement.executeQuery();
            return TfcPersistenceMapper.fromResultSetToModelList(resultSet);
        } catch (final SQLException exception) {
            throw PersistenceException.becauseFindAllFailed(exception);
        }
    }

    @Override
    public void delete(final TfcId tfcId) {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setString(1, tfcId.value());
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw PersistenceException.becauseDeleteFailed(tfcId.value(), exception);
        }
    }

    private void executeSave(final TfcPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
            statement.setString(1, dto.id());
            statement.setString(2, dto.title());
            statement.setString(3, dto.description());
            statement.setString(4, dto.advisorId());
            statement.setString(5, dto.status());
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw PersistenceException.becauseSaveFailed(dto.id(), exception);
        }
    }

    private void executeUpdate(final TfcPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, dto.title());
            statement.setString(2, dto.description());
            statement.setString(3, dto.advisorId());
            statement.setString(4, dto.status());
            statement.setString(5, dto.id());
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw PersistenceException.becauseUpdateFailed(dto.id(), exception);
        }
    }

    private TfcModel findByIdOrFail(final TfcId tfcId) {
        return getById(tfcId)
                .orElseThrow(() -> TfcNotFoundException.becauseIdWasNotFound(tfcId.value()));
    }
}