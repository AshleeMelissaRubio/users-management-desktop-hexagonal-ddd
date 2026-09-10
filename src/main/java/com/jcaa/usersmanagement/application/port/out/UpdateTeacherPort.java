package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.model.UserModel;

public interface UpdateTeacherPort {
    TeacherModel update(TeacherModel teacher);
}
