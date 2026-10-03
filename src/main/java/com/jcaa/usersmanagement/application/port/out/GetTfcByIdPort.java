package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import java.util.Optional;

public interface GetTfcByIdPort {
    Optional<TfcModel> getById(TfcId tfcId);
}