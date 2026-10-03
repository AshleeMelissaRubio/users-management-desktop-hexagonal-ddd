package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidTfcStatusException;

public enum TfcStatus {
    PROPOSED,
    IN_PROGRESS,
    SUBMITTED,
    APPROVED,
    REJECTED;

    public static TfcStatus fromString(final String value) {
        for (final TfcStatus status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw InvalidTfcStatusException.becauseValueIsInvalid();
    }
}