package com.jcaa.usersmanagement.application.port.out;

import com.jcaa.usersmanagement.domain.model.TeacherModel;

import java.util.List;

public interface GetAllTeachersPort {
    List<TeacherModel> getAll();
}
