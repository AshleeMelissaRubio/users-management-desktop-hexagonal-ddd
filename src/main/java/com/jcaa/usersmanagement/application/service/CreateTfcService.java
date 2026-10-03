package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateTfcUseCase;
import com.jcaa.usersmanagement.application.port.out.GetTfcByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveTfcPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateTfcCommand;
import com.jcaa.usersmanagement.application.service.mapper.TfcApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.TfcAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public final class CreateTfcService implements CreateTfcUseCase {

    private final SaveTfcPort saveTfcPort;
    private final GetTfcByIdPort getTfcByIdPort;
    private final Validator validator;

    @Override
    public TfcModel execute(final CreateTfcCommand command) {
        validateCommand(command);

        final TfcId tfcId = new TfcId(command.id());
        ensureTfcDoesNotExist(tfcId);

        final TfcModel tfcToSave = TfcApplicationMapper.fromCreateCommandToModel(command);
        return saveTfcPort.save(tfcToSave);
    }

    private void validateCommand(final CreateTfcCommand command) {
        final Set<ConstraintViolation<CreateTfcCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void ensureTfcDoesNotExist(final TfcId tfcId) {
        getTfcByIdPort
                .getById(tfcId)
                .ifPresent(
                        ignored -> {
                            throw TfcAlreadyExistsException.becauseTitleAlreadyExists(tfcId.value());
                        });
    }
}