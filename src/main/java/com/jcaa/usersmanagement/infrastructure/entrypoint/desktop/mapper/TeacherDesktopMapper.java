package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByDniQuery;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByIdQuery;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateTeacherRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TeacherResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateTeacherRequest;
import java.util.List;

public final class TeacherDesktopMapper {

    private TeacherDesktopMapper() {}

    public static CreateTeacherCommand toCreateCommand(final CreateTeacherRequest request) {
        return new CreateTeacherCommand(
                request.id(),
                request.dni(),
                request.name(),
                request.address());
    }

    public static UpdateTeacherCommand toUpdateCommand(final UpdateTeacherRequest request) {
        return new UpdateTeacherCommand(
                request.id(),
                request.dni(),
                request.name(),
                request.address(),
                request.status());
    }

    public static DeleteTeacherCommand toDeleteCommand(final String id) {
        return new DeleteTeacherCommand(id);
    }

    public static GetTeacherByIdQuery toGetByIdQuery(final String id) {
        return new GetTeacherByIdQuery(id);
    }

    public static GetTeacherByDniQuery toGetByDniQuery(final String dni) {
        return new GetTeacherByDniQuery(dni);
    }

    public static TeacherResponse toResponse(final TeacherModel teacher) {
        return new TeacherResponse(
                teacher.getId().value(),
                teacher.getDni().value(),
                teacher.getName().value(),
                teacher.getAddress().value(),
                teacher.getStatus().name());
    }

    public static List<TeacherResponse> toResponseList(final List<TeacherModel> teachers) {
        return teachers.stream().map(TeacherDesktopMapper::toResponse).toList();
    }
}