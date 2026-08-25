package com.jcaa.usersmanagement.domain.valueobject;

import java.util.Objects;

public record TeacherDni(String value) {

    private static final int MINIMUM_LENGTH = 7;

    public TeacherDni {
        final String normalizedValue = Objects.requireNonNull(value, "TeacherDni cannot be null").trim();
        validateNotEmpty(normalizedValue);
        validateMinimumLength(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            //throw InvalidTeacherDniException.becauseValueIsEmpty();
        }
    }

        private static void validateMinimumLength(final String normalizedValue) {
            if (normalizedValue.length() < MINIMUM_LENGTH) {
                //throw InvalidDniExceptionNameException.becauseLengthIsTooShort(MINIMUM_LENGTH);
            }
        }

    @Override
    public String toString() {
        return value;
    }
    }
