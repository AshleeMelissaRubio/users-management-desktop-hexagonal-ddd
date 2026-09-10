package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByIdQuery;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetTeacherByIdUseCase {
    TeacherModel execute(@NotNull @Valid GetTeacherByIdQuery query);
}
