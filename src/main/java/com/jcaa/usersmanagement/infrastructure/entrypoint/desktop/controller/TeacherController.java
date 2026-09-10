package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.CreateTeacherUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteTeacherUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllTeachersUseCase;
import com.jcaa.usersmanagement.application.port.in.GetTeacherByDniUseCase;
import com.jcaa.usersmanagement.application.port.in.GetTeacherByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateTeacherUseCase;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateTeacherRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TeacherResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateTeacherRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper.TeacherDesktopMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TeacherController {

    private final CreateTeacherUseCase createTeacherUseCase;
    private final UpdateTeacherUseCase updateTeacherUseCase;
    private final DeleteTeacherUseCase deleteTeacherUseCase;
    private final GetTeacherByIdUseCase getTeacherByIdUseCase;
    private final GetTeacherByDniUseCase getTeacherByDniUseCase;
    private final GetAllTeachersUseCase getAllTeachersUseCase;

    public List<TeacherResponse> listAllTeachers() {
        final var teachers = getAllTeachersUseCase.execute();
        return TeacherDesktopMapper.toResponseList(teachers);
    }

    public TeacherResponse findTeacherById(final String id) {
        final var query = TeacherDesktopMapper.toGetByIdQuery(id);
        final var teacher = getTeacherByIdUseCase.execute(query);
        return TeacherDesktopMapper.toResponse(teacher);
    }

    public TeacherResponse findTeacherByDni(final String dni) {
        final var query = TeacherDesktopMapper.toGetByDniQuery(dni);
        final var teacher = getTeacherByDniUseCase.execute(query);
        return TeacherDesktopMapper.toResponse(teacher);
    }

    public TeacherResponse createTeacher(final CreateTeacherRequest request) {
        final var command = TeacherDesktopMapper.toCreateCommand(request);
        final var teacher = createTeacherUseCase.execute(command);
        return TeacherDesktopMapper.toResponse(teacher);
    }

    public TeacherResponse updateTeacher(final UpdateTeacherRequest request) {
        final var command = TeacherDesktopMapper.toUpdateCommand(request);
        final var teacher = updateTeacherUseCase.execute(command);
        return TeacherDesktopMapper.toResponse(teacher);
    }

    public void deleteTeacher(final String id) {
        final var command = TeacherDesktopMapper.toDeleteCommand(id);
        deleteTeacherUseCase.execute(command);
    }
}
