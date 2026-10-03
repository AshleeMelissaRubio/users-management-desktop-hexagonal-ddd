package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record TfcResponse(
        String id,
        String title,
        String description,
        String advisorId,
        String status) {}