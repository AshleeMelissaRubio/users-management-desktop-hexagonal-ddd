package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TeacherModel;

public interface SaveTeacherPort {
    TeacherModel save(TeacherModel teacher);
}
