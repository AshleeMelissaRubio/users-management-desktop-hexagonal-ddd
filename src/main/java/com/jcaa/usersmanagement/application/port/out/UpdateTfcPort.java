package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TfcModel;

public interface UpdateTfcPort {
    TfcModel update(TfcModel tfc);
}