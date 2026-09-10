package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.valueobject.TeacherId;

public interface DeleteTeacherPort {
    void delete(TeacherId teacherId);
}
