package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByDniQuery;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByIdQuery;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TeacherApplicationMapper {

    public TeacherModel fromCreateCommandToModel(final CreateTeacherCommand command) {
        return TeacherModel.create(
                new TeacherId(command.id()),
                new TeacherDni(command.dni()),
                new TeacherName(command.name()),
                new TeacherAddress(command.address()));
    }

    public TeacherModel fromUpdateCommandToModel(final UpdateTeacherCommand command) {
        return new TeacherModel(
                new TeacherId(command.id()),
                new TeacherDni(command.dni()),
                new TeacherName(command.name()),
                new TeacherAddress(command.address()),
                TeacherStatus.fromString(command.status()));
    }

    public TeacherId fromGetTeacherByIdQueryToTeacherId(final GetTeacherByIdQuery query) {
        return new TeacherId(query.id());
    }

    public TeacherDni fromGetTeacherByDniQueryToTeacherDni(final GetTeacherByDniQuery query) {
        return new TeacherDni(query.dni());
    }

    public TeacherId fromDeleteCommandToTeacherId(final DeleteTeacherCommand command) {
        return new TeacherId(command.id());
    }
}
