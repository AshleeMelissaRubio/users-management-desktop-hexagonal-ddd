package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

public record TeacherEntity(
        String id,
        String dni,
        String name,
        String address,
        String status,
        String createdAt,
        String updatedAt) {}
