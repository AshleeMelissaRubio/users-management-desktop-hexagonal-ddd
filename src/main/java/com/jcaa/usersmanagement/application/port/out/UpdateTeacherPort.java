package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TeacherModel;

public interface UpdateTeacherPort {
    TeacherModel update(TeacherModel teacher);
}