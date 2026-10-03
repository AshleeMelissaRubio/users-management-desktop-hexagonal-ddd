package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidTfcTitleException;
import java.util.Objects;

public record TfcTitle(String value) {

    public TfcTitle {
        final String normalizedValue = Objects.requireNonNull(value, "TFC title cannot be null").trim();
        validateNotEmpty(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidTfcTitleException.becauseValueIsEmpty();
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
