package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.model.UserModel;

import java.util.Map;

public class TeacherUpdatedDomainEvent extends DomainEvent {

    private static final String EVENT_NAME = "teacher.updated";

    private final TeacherModel teacher;

    public TeacherUpdatedDomainEvent(final TeacherModel teacher) {
        super(EVENT_NAME);
        this.teacher = teacher;
    }

    @Override
    public Map<String, String> payload() {
        return Map.of(
                "id", teacher.getId().value(),
                "dni", teacher.getDni().value(),
                "name", teacher.getName().value(),
                "address", teacher.getAddress().value(),
                "status", teacher.getStatus().name());
    }
}
