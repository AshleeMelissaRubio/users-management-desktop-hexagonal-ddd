package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateTfcCommand;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UpdateTfcUseCase {
    TfcModel execute(@NotNull @Valid UpdateTfcCommand command);
}