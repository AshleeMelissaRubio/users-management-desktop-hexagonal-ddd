package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

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
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TfcPersistenceMapper {

    public TfcPersistenceDto fromModelToDto(final TfcModel tfc) {
        return new TfcPersistenceDto(
                tfc.getId().value(),
                tfc.getTitle().value(),
                tfc.getDescription().value(),
                tfc.getAdvisorId().value(),
                tfc.getStatus().name(),
                null,
                null);
    }

    public TfcEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
        return new TfcEntity(
                resultSet.getString("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("advisor_id"),
                resultSet.getString("status"),
                resultSet.getString("created_at"),
                resultSet.getString("updated_at"));
    }

    public TfcModel fromEntityToModel(final TfcEntity entity) {
        return new TfcModel(
                new TfcId(entity.id()),
                new TfcTitle(entity.title()),
                new TfcDescription(entity.description()),
                new TeacherId(entity.advisorId()),
                TfcStatus.fromString(entity.status()));
    }

    public TfcModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
        return fromEntityToModel(fromResultSetToEntity(resultSet));
    }

    public List<TfcModel> fromResultSetToModelList(final ResultSet resultSet) throws SQLException {
        final List<TfcModel> tfcs = new ArrayList<>();
        while (resultSet.next()) {
            tfcs.add(fromResultSetToModel(resultSet));
        }
        return tfcs;
    }
}