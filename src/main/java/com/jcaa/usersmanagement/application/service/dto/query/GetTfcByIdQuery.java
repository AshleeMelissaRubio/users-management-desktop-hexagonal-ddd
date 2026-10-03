package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;

public record GetTfcByIdQuery(
        @NotBlank(message = "id must not be blank") String id
) {

}