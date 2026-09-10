package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteTeacherPort;
import com.jcaa.usersmanagement.application.port.out.GetAllTeachersPort;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByDniPort;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveTeacherPort;
import com.jcaa.usersmanagement.application.port.out.UpdateTeacherPort;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.TeacherPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.TeacherPersistenceMapper;
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
public final class TeacherRepositoryMySQL
        implements SaveTeacherPort,
        UpdateTeacherPort,
        GetTeacherByIdPort,
        GetTeacherByDniPort,
        GetAllTeachersPort,
        DeleteTeacherPort {

    private static final String SQL_INSERT =
            "INSERT INTO teachers "
                    + "(id, dni, name, address, status, created_at, updated_at) "
                    + "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";

    private static final String SQL_UPDATE =
            "UPDATE teachers SET dni = ?, name = ?, address = ?, status = ?, updated_at = NOW() "
                    + "WHERE id = ?";

    private static final String SQL_SELECT_BY_ID =
            "SELECT id, dni, name, address, status, created_at, updated_at "
                    + "FROM teachers "
                    + "WHERE id = ? LIMIT 1";

    private static final String SQL_SELECT_BY_DNI =
            "SELECT id, dni, name, address, status, created_at, updated_at "
                    + "FROM teachers "
                    + "WHERE dni = ? LIMIT 1";

    private static final String SQL_SELECT_ALL =
            "SELECT id, dni, name, address, status, created_at, updated_at "
                    + "FROM teachers "
                    + "ORDER BY name ASC";

    private static final String SQL_DELETE =
            "DELETE FROM teachers "
                    + "WHERE id = ?";

    private final Connection connection;

    @Override
    public TeacherModel save(final TeacherModel teacher) {
        final TeacherPersistenceDto dto = TeacherPersistenceMapper.fromModelToDto(teacher);
        executeSave(dto);
        return findByIdOrFail(teacher.getId());
    }

    @Override
    public TeacherModel update(final TeacherModel teacher) {
        final TeacherPersistenceDto dto = TeacherPersistenceMapper.fromModelToDto(teacher);
        executeUpdate(dto);
        return findByIdOrFail(teacher.getId());
    }

    @Override
    public Optional<TeacherModel> getById(final TeacherId teacherId) {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setString(1, teacherId.value());
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(TeacherPersistenceMapper.fromResultSetToModel(resultSet));
        } catch (final SQLException exception) {
            throw PersistenceException.becauseFindByIdFailed(teacherId.value(), exception);
        }
    }

    @Override
    public Optional<TeacherModel> getByDni(final TeacherDni dni) {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_DNI)) {
            statement.setString(1, dni.value());
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            return Optional.of(TeacherPersistenceMapper.fromResultSetToModel(resultSet));
        } catch (final SQLException exception) {
            throw PersistenceException.becauseFindByIdFailed(dni.value(), exception);
        }
    }

    @Override
    public List<TeacherModel> getAll() {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
            final ResultSet resultSet = statement.executeQuery();
            return TeacherPersistenceMapper.fromResultSetToModelList(resultSet);
        } catch (final SQLException exception) {
            throw PersistenceException.becauseFindAllFailed(exception);
        }
    }

    @Override
    public void delete(final TeacherId teacherId) {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
            statement.setString(1, teacherId.value());
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw PersistenceException.becauseDeleteFailed(teacherId.value(), exception);
        }
    }

    private void executeSave(final TeacherPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
            statement.setString(1, dto.id());
            statement.setString(2, dto.dni());
            statement.setString(3, dto.name());
            statement.setString(4, dto.address());
            statement.setString(5, dto.status());
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw PersistenceException.becauseSaveFailed(dto.id(), exception);
        }
    }

    private void executeUpdate(final TeacherPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, dto.dni());
            statement.setString(2, dto.name());
            statement.setString(3, dto.address());
            statement.setString(4, dto.status());
            statement.setString(5, dto.id());
            statement.executeUpdate();
        } catch (final SQLException exception) {
            throw PersistenceException.becauseUpdateFailed(dto.id(), exception);
        }
    }

    private TeacherModel findByIdOrFail(final TeacherId teacherId) {
        return getById(teacherId)
                .orElseThrow(() -> TeacherNotFoundException.becauseIdWasNotFound(teacherId.value()));
    }
}