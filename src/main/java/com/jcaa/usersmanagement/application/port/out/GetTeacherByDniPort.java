package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;

import java.util.Optional;

public interface GetTeacherByDniPort {
    Optional<TeacherModel> getByDni(TeacherDni teacherDni);
}
