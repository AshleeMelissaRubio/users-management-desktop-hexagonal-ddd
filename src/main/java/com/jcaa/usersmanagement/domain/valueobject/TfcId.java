package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidTfcIdException;
import java.util.Objects;

public record TfcId(String value) {

    public TfcId {
        final String normalizedValue = Objects.requireNonNull(value, "TFC ID cannot be null").trim();
        validateNotEmpty(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidTfcIdException.becauseValueIsEmpty();
        }
    }

    @Override
    public String toString() {
        return value;
    }
}