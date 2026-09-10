package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetTeacherByDniUseCase;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByDniPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByDniQuery;
import com.jcaa.usersmanagement.application.service.mapper.TeacherApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetTeacherByDniService implements GetTeacherByDniUseCase {

    private final GetTeacherByDniPort getTeacherByDniPort;
    private final Validator validator;

    @Override
    public TeacherModel execute(final GetTeacherByDniQuery query) {
        validateQuery(query);

        final TeacherDni teacherDni = TeacherApplicationMapper.fromGetTeacherByDniQueryToTeacherDni(query);
        return getTeacherByDniPort
                .getByDni(teacherDni)
                .orElseThrow(() -> TeacherNotFoundException.becauseDniWasNotFound(teacherDni.value()));
    }

    private void validateQuery(final GetTeacherByDniQuery query) {
        final Set<ConstraintViolation<GetTeacherByDniQuery>> violations = validator.validate(query);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
