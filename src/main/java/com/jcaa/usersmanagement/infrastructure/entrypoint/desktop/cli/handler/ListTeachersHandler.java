package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.TeacherResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.TeacherController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TeacherResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListTeachersHandler implements OperationHandler {

    private final TeacherController teacherController;
    private final TeacherResponsePrinter printer;

    @Override
    public void handle() {
        final List<TeacherResponse> teachers = teacherController.listAllTeachers();
        printer.printList(teachers);
    }
}