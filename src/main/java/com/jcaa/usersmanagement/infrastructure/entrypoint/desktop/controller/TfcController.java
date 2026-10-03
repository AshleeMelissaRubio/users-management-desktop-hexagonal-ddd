package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.CreateTfcUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteTfcUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllTfcsUseCase;
import com.jcaa.usersmanagement.application.port.in.GetTfcByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateTfcUseCase;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateTfcRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TfcResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateTfcRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper.TfcDesktopMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class TfcController {

    private final CreateTfcUseCase createTfcUseCase;
    private final UpdateTfcUseCase updateTfcUseCase;
    private final DeleteTfcUseCase deleteTfcUseCase;
    private final GetTfcByIdUseCase getTfcByIdUseCase;
    private final GetAllTfcsUseCase getAllTfcsUseCase;

    public List<TfcResponse> listAllTfcs() {
        final var tfcs = getAllTfcsUseCase.execute();
        return TfcDesktopMapper.toResponseList(tfcs);
    }

    public TfcResponse findTfcById(final String id) {
        final var query = TfcDesktopMapper.toGetByIdQuery(id);
        final var tfc = getTfcByIdUseCase.execute(query);
        return TfcDesktopMapper.toResponse(tfc);
    }

    public TfcResponse createTfc(final CreateTfcRequest request) {
        final var command = TfcDesktopMapper.toCreateCommand(request);
        final var tfc = createTfcUseCase.execute(command);
        return TfcDesktopMapper.toResponse(tfc);
    }

    public TfcResponse updateTfc(final UpdateTfcRequest request) {
        final var command = TfcDesktopMapper.toUpdateCommand(request);
        final var tfc = updateTfcUseCase.execute(command);
        return TfcDesktopMapper.toResponse(tfc);
    }

    public void deleteTfc(final String id) {
        final var command = TfcDesktopMapper.toDeleteCommand(id);
        deleteTfcUseCase.execute(command);
    }
}