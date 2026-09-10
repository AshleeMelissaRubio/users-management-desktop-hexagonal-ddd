package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllTeachersUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllTeachersPort;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class GetAllTeachersService implements GetAllTeachersUseCase {

    private final GetAllTeachersPort getAllTeachersPort;

    @Override
    public List<TeacherModel> execute() {
        return getAllTeachersPort.getAll();
    }
}