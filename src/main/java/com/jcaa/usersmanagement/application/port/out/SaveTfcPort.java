package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TfcModel;

public interface SaveTfcPort {
    TfcModel save(TfcModel tfc);
}