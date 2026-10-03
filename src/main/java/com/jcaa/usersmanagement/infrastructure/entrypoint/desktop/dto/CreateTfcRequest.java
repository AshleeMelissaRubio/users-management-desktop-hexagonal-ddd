package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record CreateTfcRequest(
        String id,
        String title,
        String description,
        String advisorId) {}