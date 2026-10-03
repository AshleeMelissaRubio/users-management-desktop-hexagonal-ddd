package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetTfcByIdQuery;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetTfcByIdUseCase {
    TfcModel execute(@NotNull @Valid GetTfcByIdQuery query);
}