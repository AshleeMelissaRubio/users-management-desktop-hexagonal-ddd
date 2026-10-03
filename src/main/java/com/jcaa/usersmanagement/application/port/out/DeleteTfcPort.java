package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.TfcId;

public interface DeleteTfcPort {
    void delete(TfcId tfcId);
}