package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.model.TeacherModel;
import lombok.Getter;

import java.util.Map;

@Getter
public class TeacherCreatedDomainEvent extends DomainEvent {

    private static final String EVENT_NAME = "teacher.created";

    private final TeacherModel teacher;

    public TeacherCreatedDomainEvent(final TeacherModel teacher) {
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
