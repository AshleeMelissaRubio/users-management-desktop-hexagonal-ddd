package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteTeacherUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteTeacherPort;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTeacherCommand;
import com.jcaa.usersmanagement.application.service.mapper.TeacherApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteTeacherService implements DeleteTeacherUseCase {

    private final DeleteTeacherPort deleteTeacherPort;
    private final GetTeacherByIdPort getTeacherByIdPort;
    private final Validator validator;

    @Override
    public void execute(final DeleteTeacherCommand command) {
        validateCommand(command);

        final TeacherId teacherId = TeacherApplicationMapper.fromDeleteCommandToTeacherId(command);
        ensureTeacherExists(teacherId);
        deleteTeacherPort.delete(teacherId);
    }

    private void validateCommand(final DeleteTeacherCommand command) {
        final Set<ConstraintViolation<DeleteTeacherCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void ensureTeacherExists(final TeacherId teacherId) {
        getTeacherByIdPort
                .getById(teacherId)
                .orElseThrow(() -> TeacherNotFoundException.becauseIdWasNotFound(teacherId.value()));
    }
}