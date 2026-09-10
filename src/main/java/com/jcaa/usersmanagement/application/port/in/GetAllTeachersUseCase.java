package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.TeacherModel;

import java.util.List;

public interface GetAllTeachersUseCase {
    List<TeacherModel> execute();
}
