package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UpdateTeacherUseCase {

    TeacherModel execute(@NotNull @Valid UpdateTeacherCommand command);
}
