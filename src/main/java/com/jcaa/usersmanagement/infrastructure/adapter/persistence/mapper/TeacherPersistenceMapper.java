package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

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
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TeacherPersistenceMapper {

    public TeacherPersistenceDto fromModelToDto(final TeacherModel teacher) {
        return new TeacherPersistenceDto(
                teacher.getId().value(),
                teacher.getDni().value(),
                teacher.getName().value(),
                teacher.getAddress().value(),
                teacher.getStatus().name(),
                null,
                null);
    }

    public TeacherEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
        return new TeacherEntity(
                resultSet.getString("id"),
                resultSet.getString("dni"),
                resultSet.getString("name"),
                resultSet.getString("address"),
                resultSet.getString("status"),
                resultSet.getString("created_at"),
                resultSet.getString("updated_at"));
    }

    public TeacherModel fromEntityToModel(final TeacherEntity entity) {
        return new TeacherModel(
                new TeacherId(entity.id()),
                new TeacherDni(entity.dni()),
                new TeacherName(entity.name()),
                new TeacherAddress(entity.address()),
                TeacherStatus.fromString(entity.status()));
    }

    public TeacherModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
        return fromEntityToModel(fromResultSetToEntity(resultSet));
    }

    public List<TeacherModel> fromResultSetToModelList(final ResultSet resultSet) throws SQLException {
        final List<TeacherModel> teachers = new ArrayList<>();
        while (resultSet.next()) {
            teachers.add(fromResultSetToModel(resultSet));
        }
        return teachers;
    }
}
