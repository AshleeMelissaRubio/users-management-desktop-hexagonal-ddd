package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.TfcModel;
import java.util.List;

public interface GetAllTfcsUseCase {
    List<TfcModel> execute();
}