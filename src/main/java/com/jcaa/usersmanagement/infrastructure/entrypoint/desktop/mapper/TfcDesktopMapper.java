package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetTfcByIdQuery;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateTfcRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TfcResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateTfcRequest;
import java.util.List;

public final class TfcDesktopMapper {

    private TfcDesktopMapper() {}

    public static CreateTfcCommand toCreateCommand(final CreateTfcRequest request) {
        return new CreateTfcCommand(
                request.id(), request.title(), request.description(), request.advisorId());
    }

    public static UpdateTfcCommand toUpdateCommand(final UpdateTfcRequest request) {
        return new UpdateTfcCommand(
                request.id(), request.title(), request.description(), request.advisorId());
    }

    public static DeleteTfcCommand toDeleteCommand(final String id) {
        return new DeleteTfcCommand(id);
    }

    public static GetTfcByIdQuery toGetByIdQuery(final String id) {
        return new GetTfcByIdQuery(id);
    }

    public static TfcResponse toResponse(final TfcModel tfc) {
        return new TfcResponse(
                tfc.getId().value(),
                tfc.getTitle().value(),
                tfc.getDescription().value(),
                tfc.getAdvisorId().value(),
                tfc.getStatus().name());
    }

    public static List<TfcResponse> toResponseList(final List<TfcModel> tfcs) {
        return tfcs.stream().map(TfcDesktopMapper::toResponse).toList();
    }
}