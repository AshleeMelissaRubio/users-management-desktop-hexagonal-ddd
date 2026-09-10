package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTeacherCommand(
        @NotBlank(message = "id must not be blank") String id,
        @NotBlank(message = "dni must not be blank") String dni,
        @Size(min = 3, message = "name must have at least 3 characters") String name,
        @NotBlank(message = "address must not be blank") String address,
        @NotBlank(message = "status must not be blank") String status)
{
}
