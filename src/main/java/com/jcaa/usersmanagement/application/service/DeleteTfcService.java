package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteTfcUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteTfcPort;
import com.jcaa.usersmanagement.application.port.out.GetTfcByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTfcCommand;
import com.jcaa.usersmanagement.application.service.mapper.TfcApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class DeleteTfcService implements DeleteTfcUseCase {

    private final DeleteTfcPort deleteTfcPort;
    private final GetTfcByIdPort getTfcByIdPort;
    private final Validator validator;

    @Override
    public void execute(final DeleteTfcCommand command) {
        validateCommand(command);

        final TfcId tfcId = TfcApplicationMapper.fromDeleteCommandToTfcId(command);
        ensureTfcExists(tfcId);
        deleteTfcPort.delete(tfcId);
    }

    private void validateCommand(final DeleteTfcCommand command) {
        final Set<ConstraintViolation<DeleteTfcCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void ensureTfcExists(final TfcId tfcId) {
        getTfcByIdPort
                .getById(tfcId)
                .orElseThrow(() -> TfcNotFoundException.becauseIdWasNotFound(tfcId.value()));
    }
}