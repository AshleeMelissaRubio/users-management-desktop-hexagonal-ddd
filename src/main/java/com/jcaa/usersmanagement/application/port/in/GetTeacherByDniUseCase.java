package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByDniQuery;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetTeacherByDniUseCase {
    TeacherModel execute(@NotNull @Valid GetTeacherByDniQuery query);
}
