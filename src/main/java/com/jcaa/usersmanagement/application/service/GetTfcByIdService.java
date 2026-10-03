package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetTfcByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetTfcByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetTfcByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.TfcApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetTfcByIdService implements GetTfcByIdUseCase {

    private final GetTfcByIdPort getTfcByIdPort;
    private final Validator validator;

    @Override
    public TfcModel execute(final GetTfcByIdQuery query) {
        validateQuery(query);

        final TfcId tfcId = TfcApplicationMapper.fromGetTfcByIdQueryToTfcId(query);
        return getTfcByIdPort
                .getById(tfcId)
                .orElseThrow(() -> TfcNotFoundException.becauseIdWasNotFound(tfcId.value()));
    }

    private void validateQuery(final GetTfcByIdQuery query) {
        final Set<ConstraintViolation<GetTfcByIdQuery>> violations = validator.validate(query);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}