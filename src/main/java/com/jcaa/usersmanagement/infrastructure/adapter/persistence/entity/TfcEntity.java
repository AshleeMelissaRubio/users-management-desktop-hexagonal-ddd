package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

public record TfcEntity(
        String id,
        String title,
        String description,
        String advisorId,
        String status,
        String createdAt,
        String updatedAt) {}