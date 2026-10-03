package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTfcCommand(
        @NotBlank(message = "id must not be blank") String id,
        @NotBlank(message = "title must not be blank")
        @Size(min = 5, message = "title must have at least 5 characters")
        String title,
        @NotBlank(message = "description must not be blank")
        @Size(min = 10, message = "description must have at least 10 characters")
        String description,
        @NotBlank(message = "advisorId must not be blank") String advisorId
) {

}