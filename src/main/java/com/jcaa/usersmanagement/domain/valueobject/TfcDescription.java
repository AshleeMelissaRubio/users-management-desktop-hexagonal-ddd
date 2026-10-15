package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidTfcDescriptionException;
import com.jcaa.usersmanagement.domain.exception.InvalidTfcTitleException;

import java.util.Objects;

public record TfcDescription(String value) {

    private static final int MINIMUM_LENGTH = 10;

    public TfcDescription {
        final String normalizedValue = Objects.requireNonNull(value, "TFC description cannot be null").trim();
        validateNotEmpty(normalizedValue);
        validateMinimumLength(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidTfcDescriptionException.becauseValueIsEmpty();
        }
    }

    private static void validateMinimumLength(final String normalizedValue) {
        if (normalizedValue.length() < MINIMUM_LENGTH) {
            throw InvalidTfcDescriptionException.becauseLengthIsTooShort(MINIMUM_LENGTH);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}