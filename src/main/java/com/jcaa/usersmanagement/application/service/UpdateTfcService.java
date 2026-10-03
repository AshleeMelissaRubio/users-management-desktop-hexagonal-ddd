package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateTfcUseCase;
import com.jcaa.usersmanagement.application.port.out.GetTfcByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateTfcPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTfcCommand;
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
public final class UpdateTfcService implements UpdateTfcUseCase {

    private final UpdateTfcPort updateTfcPort;
    private final GetTfcByIdPort getTfcByIdPort;
    private final Validator validator;

    @Override
    public TfcModel execute(final UpdateTfcCommand command) {
        validateCommand(command);

        final TfcId tfcId = new TfcId(command.id());
        final TfcModel current = findExistingTfcOrFail(tfcId);

        final TfcModel tfcToUpdate =
                TfcApplicationMapper.fromUpdateCommandToModel(command, current.getStatus());

        return updateTfcPort.update(tfcToUpdate);
    }

    private void validateCommand(final UpdateTfcCommand command) {
        final Set<ConstraintViolation<UpdateTfcCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private TfcModel findExistingTfcOrFail(final TfcId tfcId) {
        return getTfcByIdPort
                .getById(tfcId)
                .orElseThrow(() -> TfcNotFoundException.becauseIdWasNotFound(tfcId.value()));
    }
}