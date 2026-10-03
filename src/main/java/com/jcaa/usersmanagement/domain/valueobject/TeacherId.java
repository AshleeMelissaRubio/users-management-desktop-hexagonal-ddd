package com.jcaa.usersmanagement.domain.valueobject;

/**
 * Temporal Stub/Dummy para el módulo TFC.
 * Permite compilar y probar la rama feature/tfc de forma autónoma.
 * Será reemplazado automáticamente al hacer merge con la rama del módulo Teacher.
 */
public record TeacherId(String value) {
    public TeacherId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Teacher ID cannot be null or empty");
        }
    }
}