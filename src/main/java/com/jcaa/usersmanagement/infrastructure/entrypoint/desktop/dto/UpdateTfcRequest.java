package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record UpdateTfcRequest(
        String id,
        String title,
        String description,
        String advisorId) {}