package com.jcaa.usersmanagement.domain.valueobject;

import java.util.Objects;

public record TeacherName(String value) {

    private static final int MINIMUM_LENGTH = 3;

    public TeacherName {
        final String normalizedValue = Objects.requireNonNull(value, "UserName cannot be null").trim();
        validateNotEmpty(normalizedValue);
        validateMinimumLength(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            //throw InvalidTeacherNameException.becauseValueIsEmpty();
        }
    }

    private static void validateMinimumLength(final String normalizedValue) {
        if (normalizedValue.length() < MINIMUM_LENGTH) {
            //throw InvalidTeacherNameException.becauseLengthIsTooShort(MINIMUM_LENGTH);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
