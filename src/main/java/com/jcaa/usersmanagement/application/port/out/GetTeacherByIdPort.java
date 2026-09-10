package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;

import java.util.Optional;

public interface GetTeacherByIdPort {
    Optional<TeacherModel> getById(TeacherId teacherId);
}
