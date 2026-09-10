package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record CreateTeacherRequest(
        String id,
        String dni,
        String name,
        String address) {}