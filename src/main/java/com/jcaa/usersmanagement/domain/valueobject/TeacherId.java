package com.jcaa.usersmanagement.domain.valueobject;

import java.util.Objects;

public record TeacherId(String value) {
    public TeacherId {
        final String normalizedValue = Objects.requireNonNull(value, "UserId cannot be null").trim();
        validateNotEmpty(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            //throw InvalidTeacherIdException.becauseValueIsEmpty();
        }
    }

    @Override
    public String toString() {
        return value;
    }
}

