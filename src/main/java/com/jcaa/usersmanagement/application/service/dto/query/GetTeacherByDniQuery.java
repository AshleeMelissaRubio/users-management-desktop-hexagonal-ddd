package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;

public record GetTeacherByDniQuery(@NotBlank(message = "dni must not be blank") String dni) {
}
