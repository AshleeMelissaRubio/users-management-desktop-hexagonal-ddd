package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record TeacherResponse(
        String id,
        String dni,
        String name,
        String address,
        String status) {}