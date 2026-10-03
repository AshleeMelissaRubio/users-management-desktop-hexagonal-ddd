package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.CreateTfcCommand;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CreateTfcUseCase {
    TfcModel execute(@NotNull @Valid CreateTfcCommand command);
}