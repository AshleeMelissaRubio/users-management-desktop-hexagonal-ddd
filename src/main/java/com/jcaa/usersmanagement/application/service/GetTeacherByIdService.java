package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetTeacherByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.TeacherApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetTeacherByIdService implements GetTeacherByIdUseCase {

    private final GetTeacherByIdPort getTeacherByIdPort;
    private final Validator validator;

    @Override
    public TeacherModel execute(final GetTeacherByIdQuery query) {
        validateQuery(query);

        final TeacherId teacherId = TeacherApplicationMapper.fromGetTeacherByIdQueryToTeacherId(query);
        return getTeacherByIdPort
                .getById(teacherId)
                .orElseThrow(() -> TeacherNotFoundException.becauseIdWasNotFound(teacherId.value()));
    }

    private void validateQuery(final GetTeacherByIdQuery query) {
        final Set<ConstraintViolation<GetTeacherByIdQuery>> violations = validator.validate(query);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}