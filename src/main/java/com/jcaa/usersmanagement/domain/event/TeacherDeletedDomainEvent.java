package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import lombok.Getter;
import java.util.Map;

@Getter
public class TeacherDeletedDomainEvent extends DomainEvent {

    private static final String EVENT_NAME = "teacher.deleted";

    private final TeacherId teacherId;

    public TeacherDeletedDomainEvent(final TeacherId teacherId) {
        super(EVENT_NAME);
        this.teacherId = teacherId;
    }

    @Override
    public Map<String, String> payload() { return Map.of("id", teacherId.value()); }
}
