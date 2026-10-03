package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidTfcDescriptionException;
import java.util.Objects;

public record TfcDescription(String value) {

    public TfcDescription {
        final String normalizedValue = Objects.requireNonNull(value, "TFC description cannot be null").trim();
        validateNotEmpty(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidTfcDescriptionException.becauseValueIsEmpty();
        }
    }

    @Override
    public String toString() {
        return value;
    }
}