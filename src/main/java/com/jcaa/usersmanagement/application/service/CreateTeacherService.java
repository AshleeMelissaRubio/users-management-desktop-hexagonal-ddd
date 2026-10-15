package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateTeacherUseCase;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByDniPort;
import com.jcaa.usersmanagement.application.port.out.SaveTeacherPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateTeacherCommand;
import com.jcaa.usersmanagement.application.service.mapper.TeacherApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.TeacherAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public final class CreateTeacherService implements CreateTeacherUseCase {

    private final SaveTeacherPort saveTeacherPort;
    private final GetTeacherByDniPort getTeacherByDniPort;
    private final Validator validator;

    @Override
    public TeacherModel execute(final CreateTeacherCommand command) {
        validateCommand(command);

        final TeacherDni dni = new TeacherDni(command.dni());
        ensureDniIsNotTaken(dni);

        final TeacherModel teacherToSave = TeacherApplicationMapper.fromCreateCommandToModel(command);
        return saveTeacherPort.save(teacherToSave);
    }

    private void validateCommand(final CreateTeacherCommand command) {
        final Set<ConstraintViolation<CreateTeacherCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void ensureDniIsNotTaken(final TeacherDni dni) {
        getTeacherByDniPort
                .getByDni(dni)
                .ifPresent(
                        ignored -> {
                            throw TeacherAlreadyExistsException.becauseDniAlreadyExists(dni.value());
                        });
    }
}