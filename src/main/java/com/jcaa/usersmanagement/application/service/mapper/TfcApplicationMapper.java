package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetTfcByIdQuery;
import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TfcDescription;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.domain.valueobject.TfcTitle;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TfcApplicationMapper {

    public TfcModel fromCreateCommandToModel(final CreateTfcCommand command) {
        return TfcModel.create(
                new TfcId(command.id()),
                new TfcTitle(command.title()),
                new TfcDescription(command.description()),
                new TeacherId(command.advisorId()));
    }

    public TfcModel fromUpdateCommandToModel(
            final UpdateTfcCommand command, final TfcStatus currentStatus) {
        return new TfcModel(
                new TfcId(command.id()),
                new TfcTitle(command.title()),
                new TfcDescription(command.description()),
                new TeacherId(command.advisorId()),
                currentStatus);
    }

    public TfcId fromGetTfcByIdQueryToTfcId(final GetTfcByIdQuery query) {
        return new TfcId(query.id());
    }

    public TfcId fromDeleteCommandToTfcId(final DeleteTfcCommand command) {
        return new TfcId(command.id());
    }
}