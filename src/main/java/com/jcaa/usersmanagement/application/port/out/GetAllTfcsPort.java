package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TfcModel;
import java.util.List;

public interface GetAllTfcsPort {
    List<TfcModel> getAll();
}