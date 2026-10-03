package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllTfcsUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllTfcsPort;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetAllTfcsService implements GetAllTfcsUseCase {

    private final GetAllTfcsPort getAllTfcsPort;

    @Override
    public List<TfcModel> execute() {
        return getAllTfcsPort.getAll();
    }
}