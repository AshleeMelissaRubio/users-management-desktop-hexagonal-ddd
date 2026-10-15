package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jcaa.usersmanagement.application.port.in.*;
import com.jcaa.usersmanagement.application.service.dto.command.CreateTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByDniQuery;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByIdQuery;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.exception.TeacherAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateTeacherRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TeacherResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateTeacherRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for TeacherController.
 *
 * <p>Covers: correct delegation to every use-case port, accurate DTO→command/query mapping,
 * accurate domain-model→response mapping, and transparent exception propagation. All ports are
 * mocked; no infrastructure is exercised.
 */
@DisplayName("TeacherController")
@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock private CreateTeacherUseCase createTeacherUseCase;
    @Mock private UpdateTeacherUseCase updateTeacherUseCase;
    @Mock private DeleteTeacherUseCase deleteTeacherUseCase;
    @Mock private GetTeacherByIdUseCase getTeacherByIdUseCase;
    @Mock private GetTeacherByDniUseCase getTeacherByDniUseCase;
    @Mock private GetAllTeachersUseCase getAllTeachersUseCase;

    private TeacherController controller;

    // ── helpers

    private static TeacherModel buildTeacher(
            final String id,
            final String dni,
            final String name,
            final String address,
            final TeacherStatus status) {
        return new TeacherModel(
                new TeacherId(id),
                new TeacherDni(dni),
                new TeacherName(name),
                new TeacherAddress(address),
                status);
    }

    @BeforeEach
    void setUp() {
        controller =
                new TeacherController(
                        createTeacherUseCase,
                        updateTeacherUseCase,
                        deleteTeacherUseCase,
                        getTeacherByIdUseCase,
                        getTeacherByDniUseCase,
                        getAllTeachersUseCase);
    }

    // ── listAllTeachers

    @Test
    @DisplayName(
            "listAllTeachers() returns a correctly mapped TeacherResponse list when the use case returns teachers")
    void listAllTeachers_returnsMappedResponseList_whenTeachersExist() {
        // Arrange
        final TeacherModel teacher =
                buildTeacher("t-001", "12345678A", "Alice Smith", "Calle 123", TeacherStatus.ACTIVE);
        when(getAllTeachersUseCase.execute()).thenReturn(List.of(teacher));

        // Act
        final List<TeacherResponse> result = controller.listAllTeachers();

        // Assert
        assertAll(
                "single-teacher list mapping",
                () -> assertEquals(1, result.size(), "list must contain exactly one element"),
                () -> assertEquals("t-001", result.get(0).id(), "id must match"),
                () -> assertEquals("12345678A", result.get(0).dni(), "dni must match"),
                () -> assertEquals("Alice Smith", result.get(0).name(), "name must match"),
                () -> assertEquals("Calle 123", result.get(0).address(), "address must match"),
                () -> assertEquals("ACTIVE", result.get(0).status(), "status must match enum name"));
        verify(getAllTeachersUseCase).execute();
    }

    @Test
    @DisplayName("listAllTeachers() returns an empty list when the use case returns no teachers")
    void listAllTeachers_returnsEmptyList_whenNoTeachersExist() {
        // Arrange
        when(getAllTeachersUseCase.execute()).thenReturn(List.of());

        // Act
        final List<TeacherResponse> result = controller.listAllTeachers();

        // Assert
        assertTrue(result.isEmpty(), "result must be an empty list");
        verify(getAllTeachersUseCase).execute();
    }

    // ── findTeacherById

    @Test
    @DisplayName(
            "findTeacherById() builds a GetTeacherByIdQuery with the given id and returns the mapped response")
    void findTeacherById_returnsMappedResponse_whenTeacherExists() {
        // Arrange
        final TeacherModel teacher =
                buildTeacher("t-002", "87654321B", "Bob Jones", "Avenida 45", TeacherStatus.ACTIVE);
        when(getTeacherByIdUseCase.execute(new GetTeacherByIdQuery("t-002"))).thenReturn(teacher);

        // Act
        final TeacherResponse result = controller.findTeacherById("t-002");

        // Assert
        assertAll(
                "findTeacherById response mapping",
                () -> assertEquals("t-002", result.id(), "id must match"),
                () -> assertEquals("87654321B", result.dni(), "dni must match"),
                () -> assertEquals("Bob Jones", result.name(), "name must match"),
                () -> assertEquals("Avenida 45", result.address(), "address must match"),
                () -> assertEquals("ACTIVE", result.status(), "status must match enum name"));
    }

    @Test
    @DisplayName(
            "findTeacherById() propagates TeacherNotFoundException when the use case cannot find the teacher")
    void findTeacherById_propagatesTeacherNotFoundException_whenTeacherDoesNotExist() {
        // Arrange
        when(getTeacherByIdUseCase.execute(new GetTeacherByIdQuery("t-999")))
                .thenThrow(TeacherNotFoundException.becauseIdWasNotFound("t-999"));

        // Act & Assert
        assertThrows(
                TeacherNotFoundException.class,
                () -> controller.findTeacherById("t-999"),
                "TeacherNotFoundException must propagate without being wrapped");
    }

    // ── findTeacherByDni

    @Test
    @DisplayName(
            "findTeacherByDni() builds a GetTeacherByDniQuery with the given dni and returns the mapped response")
    void findTeacherByDni_returnsMappedResponse_whenTeacherExists() {
        // Arrange
        final TeacherModel teacher =
                buildTeacher("t-003", "11223344C", "Carol White", "Carrera 7", TeacherStatus.ACTIVE);
        when(getTeacherByDniUseCase.execute(new GetTeacherByDniQuery("11223344C"))).thenReturn(teacher);

        // Act
        final TeacherResponse result = controller.findTeacherByDni("11223344C");

        // Assert
        assertAll(
                "findTeacherByDni response mapping",
                () -> assertEquals("t-003", result.id(), "id must match"),
                () -> assertEquals("11223344C", result.dni(), "dni must match"),
                () -> assertEquals("Carol White", result.name(), "name must match"),
                () -> assertEquals("Carrera 7", result.address(), "address must match"),
                () -> assertEquals("ACTIVE", result.status(), "status must match enum name"));
    }

    @Test
    @DisplayName(
            "findTeacherByDni() propagates TeacherNotFoundException when the use case cannot find the teacher")
    void findTeacherByDni_propagatesTeacherNotFoundException_whenTeacherDoesNotExist() {
        // Arrange
        when(getTeacherByDniUseCase.execute(new GetTeacherByDniQuery("99999999Z")))
                .thenThrow(TeacherNotFoundException.becauseDniWasNotFound("99999999Z"));

        // Act & Assert
        assertThrows(
                TeacherNotFoundException.class,
                () -> controller.findTeacherByDni("99999999Z"),
                "TeacherNotFoundException must propagate without being wrapped");
    }

    // ── createTeacher

    @Test
    @DisplayName(
            "createTeacher() delegates a correctly populated CreateTeacherCommand and returns the mapped response")
    void createTeacher_delegatesCorrectCommandAndReturnsMappedResponse_whenCreationSucceeds() {
        // Arrange
        final CreateTeacherRequest request =
                new CreateTeacherRequest("t-004", "44332211D", "Dave Brown", "Diagonal 12");
        final TeacherModel createdTeacher =
                buildTeacher("t-004", "44332211D", "Dave Brown", "Diagonal 12", TeacherStatus.ACTIVE);
        final ArgumentCaptor<CreateTeacherCommand> captor =
                ArgumentCaptor.forClass(CreateTeacherCommand.class);
        when(createTeacherUseCase.execute(captor.capture())).thenReturn(createdTeacher);

        // Act
        final TeacherResponse result = controller.createTeacher(request);

        // Assert
        assertAll(
                "createTeacher command delegation and response mapping",
                () -> assertEquals("t-004", captor.getValue().id(), "command id must match request id"),
                () -> assertEquals("44332211D", captor.getValue().dni(), "command dni must match request dni"),
                () ->
                        assertEquals(
                                "Dave Brown", captor.getValue().name(), "command name must match request name"),
                () ->
                        assertEquals(
                                "Diagonal 12",
                                captor.getValue().address(),
                                "command address must match request address"),
                () ->
                        assertEquals(
                                "t-004",
                                result.id(),
                                "response id must come from the domain model returned by use case"),
                () ->
                        assertEquals(
                                "ACTIVE",
                                result.status(),
                                "response status must reflect the domain model status"));
    }

    @Test
    @DisplayName(
            "createTeacher() propagates TeacherAlreadyExistsException when the use case rejects a duplicate DNI")
    void createTeacher_propagatesTeacherAlreadyExistsException_whenDniIsDuplicated() {
        // Arrange
        final CreateTeacherRequest request =
                new CreateTeacherRequest("t-005", "12345678A", "Eve Martinez", "Calle 50");
        when(createTeacherUseCase.execute(any()))
                .thenThrow(TeacherAlreadyExistsException.becauseDniAlreadyExists("12345678A"));

        // Act & Assert
        assertThrows(
                TeacherAlreadyExistsException.class,
                () -> controller.createTeacher(request),
                "TeacherAlreadyExistsException must propagate without being wrapped");
    }

    // ── updateTeacher

    @Test
    @DisplayName(
            "updateTeacher() delegates a correctly populated UpdateTeacherCommand and returns the mapped response")
    void updateTeacher_delegatesCorrectCommandAndReturnsMappedResponse_whenUpdateSucceeds() {
        // Arrange
        final UpdateTeacherRequest request =
                new UpdateTeacherRequest("t-006", "55667788E", "Frank Green", "Transversal 8", "INACTIVE");
        final TeacherModel updatedTeacher =
                buildTeacher("t-006", "55667788E", "Frank Green", "Transversal 8", TeacherStatus.INACTIVE);
        final ArgumentCaptor<UpdateTeacherCommand> captor =
                ArgumentCaptor.forClass(UpdateTeacherCommand.class);
        when(updateTeacherUseCase.execute(captor.capture())).thenReturn(updatedTeacher);

        // Act
        final TeacherResponse result = controller.updateTeacher(request);

        // Assert
        assertAll(
                "updateTeacher command delegation and response mapping",
                () -> assertEquals("t-006", captor.getValue().id(), "command id must match request id"),
                () -> assertEquals("55667788E", captor.getValue().dni(), "command dni must match request dni"),
                () ->
                        assertEquals(
                                "Frank Green", captor.getValue().name(), "command name must match request name"),
                () ->
                        assertEquals(
                                "Transversal 8",
                                captor.getValue().address(),
                                "command address must match request address"),
                () ->
                        assertEquals(
                                "INACTIVE", captor.getValue().status(), "command status must match request status"),
                () ->
                        assertEquals(
                                "t-006",
                                result.id(),
                                "response id must come from the domain model returned by use case"),
                () ->
                        assertEquals(
                                "INACTIVE", result.status(), "response status must reflect the domain model status"));
    }

    @Test
    @DisplayName(
            "updateTeacher() propagates TeacherNotFoundException when the use case cannot find the teacher")
    void updateTeacher_propagatesTeacherNotFoundException_whenTeacherDoesNotExist() {
        // Arrange
        final UpdateTeacherRequest request =
                new UpdateTeacherRequest("t-999", "99999999Z", "Ghost Teacher", "Unknown Address", "INACTIVE");
        when(updateTeacherUseCase.execute(any()))
                .thenThrow(TeacherNotFoundException.becauseIdWasNotFound("t-999"));

        // Act & Assert
        assertThrows(
                TeacherNotFoundException.class,
                () -> controller.updateTeacher(request),
                "TeacherNotFoundException must propagate without being wrapped");
    }

    // ── deleteTeacher

    @Test
    @DisplayName("deleteTeacher() delegates a DeleteTeacherCommand with the given id to the use case")
    void deleteTeacher_delegatesDeleteCommandWithCorrectId() {
        // Arrange
        final ArgumentCaptor<DeleteTeacherCommand> captor =
                ArgumentCaptor.forClass(DeleteTeacherCommand.class);
        doNothing().when(deleteTeacherUseCase).execute(captor.capture());

        // Act
        controller.deleteTeacher("t-007");

        // Assert
        assertEquals("t-007", captor.getValue().id(), "delete command id must match the provided id");
    }

    @Test
    @DisplayName(
            "deleteTeacher() propagates TeacherNotFoundException when the use case cannot find the teacher")
    void deleteTeacher_propagatesTeacherNotFoundException_whenTeacherDoesNotExist() {
        // Arrange
        doThrow(TeacherNotFoundException.becauseIdWasNotFound("t-999"))
                .when(deleteTeacherUseCase)
                .execute(any());

        // Act & Assert
        assertThrows(
                TeacherNotFoundException.class,
                () -> controller.deleteTeacher("t-999"),
                "TeacherNotFoundException must propagate without being wrapped");
    }
}