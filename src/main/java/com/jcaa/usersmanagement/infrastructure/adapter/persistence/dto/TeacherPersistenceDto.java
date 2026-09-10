package com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto;

public record TeacherPersistenceDto(
        String id,
        String dni,
        String name,
        String address,
        String status,
        String createdAt,
        String updatedAt) {}