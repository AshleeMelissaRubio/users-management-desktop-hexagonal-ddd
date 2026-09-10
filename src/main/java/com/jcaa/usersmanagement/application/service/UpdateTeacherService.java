package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateTeacherUseCase;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByDniPort;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateTeacherPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTeacherCommand;
import com.jcaa.usersmanagement.application.service.mapper.TeacherApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.TeacherAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateTeacherService implements UpdateTeacherUseCase {

    private final UpdateTeacherPort updateTeacherPort;
    private final GetTeacherByIdPort getTeacherByIdPort;
    private final GetTeacherByDniPort getTeacherByDniPort;
    private final Validator validator;

    @Override
    public TeacherModel execute(final UpdateTeacherCommand command) {
        validateCommand(command);

        final TeacherId teacherId = new TeacherId(command.id());
        final TeacherModel current = findExistingTeacherOrFail(teacherId);
        final TeacherDni newDni = new TeacherDni(command.dni());

        ensureDniIsNotTakenByAnotherTeacher(newDni, teacherId);

        final TeacherModel teacherToUpdate = TeacherApplicationMapper.fromUpdateCommandToModel(command);
        return updateTeacherPort.update(teacherToUpdate);
    }

    private void validateCommand(final UpdateTeacherCommand command) {
        final Set<ConstraintViolation<UpdateTeacherCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private TeacherModel findExistingTeacherOrFail(final TeacherId teacherId) {
        return getTeacherByIdPort
                .getById(teacherId)
                .orElseThrow(() -> TeacherNotFoundException.becauseIdWasNotFound(teacherId.value()));
    }

    private void ensureDniIsNotTakenByAnotherTeacher(final TeacherDni newDni, final TeacherId ownerId) {
        getTeacherByDniPort
                .getByDni(newDni)
                .ifPresent(
                        found -> {
                            if (!found.getId().equals(ownerId)) {
                                throw TeacherAlreadyExistsException.becauseDniAlreadyExists(newDni.value());
                            }
                        });
    }
}