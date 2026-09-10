package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateTeacherAddressCommand;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UpdateTeacherAddressUseCase {

    TeacherModel execute(@NotNull @Valid UpdateTeacherAddressCommand command);
}
