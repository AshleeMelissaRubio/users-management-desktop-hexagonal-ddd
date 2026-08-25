package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidUserStatusException;

public enum TeacherStatus {
    ACTIVE,
    INACTIVE,
    PENDING,
    BLOCKED;

    public static TeacherStatus fromString(final String value) {
        for (final TeacherStatus status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        //throw InvalidTeacherStatusException.becauseValueIsInvalid(value);
    }
}
