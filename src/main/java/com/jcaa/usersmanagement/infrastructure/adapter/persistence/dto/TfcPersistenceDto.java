package com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto;

public record TfcPersistenceDto(
        String id,
        String title,
        String description,
        String advisorId,
        String status,
        String createdAt,
        String updatedAt) {}