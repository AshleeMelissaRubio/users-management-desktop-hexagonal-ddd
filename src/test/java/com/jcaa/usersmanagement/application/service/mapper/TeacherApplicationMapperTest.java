package com.jcaa.usersmanagement.application.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.application.service.dto.command.CreateTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTeacherCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByDniQuery;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByIdQuery;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TeacherApplicationMapper.
 *
 * <p>Cubre cada método público de conversión mapeando comandos y queries a Value Objects y modelos
 * de dominio (TeacherModel).
 */
@DisplayName("TeacherApplicationMapper")
class TeacherApplicationMapperTest {

    private static final String ID = "t-001";
    private static final String DNI = "12345678A";
    private static final String NAME = "Carlos Pérez";
    private static final String ADDRESS = "Calle 123 #45-67";
    private static final String STATUS = "ACTIVE";

    // ── fromCreateCommandToModel()

    @Test
    @DisplayName("fromCreateCommandToModel() mapea todos los campos y fija status PENDING")
    void shouldMapCreateCommandToModelWithPendingStatus() {
        // Arrange
        final CreateTeacherCommand command = new CreateTeacherCommand(ID, DNI, NAME, ADDRESS);

        // Act
        final TeacherModel result = TeacherApplicationMapper.fromCreateCommandToModel(command);

        // Assert
        assertAll(
                "fromCreateCommandToModel()",
                () -> assertEquals(ID, result.getId().value(), "id"),
                () -> assertEquals(DNI, result.getDni().value(), "dni"),
                () -> assertEquals(NAME, result.getName().value(), "name"),
                () -> assertEquals(ADDRESS, result.getAddress().value(), "address"),
                () -> assertEquals(TeacherStatus.PENDING, result.getStatus(), "status debe ser PENDING"));
    }

    // ── fromUpdateCommandToModel()

    @Test
    @DisplayName("fromUpdateCommandToModel() mapea correctamente todos los campos a TeacherModel")
    void shouldMapUpdateCommandToModel() {
        // Arrange
        final UpdateTeacherCommand command = new UpdateTeacherCommand(ID, DNI, NAME, ADDRESS, STATUS);

        // Act
        final TeacherModel result = TeacherApplicationMapper.fromUpdateCommandToModel(command);

        // Assert
        assertAll(
                "fromUpdateCommandToModel()",
                () -> assertEquals(ID, result.getId().value(), "id"),
                () -> assertEquals(DNI, result.getDni().value(), "dni"),
                () -> assertEquals(NAME, result.getName().value(), "name"),
                () -> assertEquals(ADDRESS, result.getAddress().value(), "address"),
                () -> assertEquals(TeacherStatus.ACTIVE, result.getStatus(), "status"));
    }

    // ── fromGetTeacherByIdQueryToTeacherId()

    @Test
    @DisplayName("fromGetTeacherByIdQueryToTeacherId() extrae el TeacherId del query")
    void shouldExtractTeacherIdFromQuery() {
        // Arrange
        final GetTeacherByIdQuery query = new GetTeacherByIdQuery(ID);

        // Act
        final TeacherId result = TeacherApplicationMapper.fromGetTeacherByIdQueryToTeacherId(query);

        // Assert
        assertEquals(ID, result.value(), "id debe coincidir con el del query");
    }

    // ── fromGetTeacherByDniQueryToTeacherDni()

    @Test
    @DisplayName("fromGetTeacherByDniQueryToTeacherDni() extrae el TeacherDni del query")
    void shouldExtractTeacherDniFromQuery() {
        // Arrange
        final GetTeacherByDniQuery query = new GetTeacherByDniQuery(DNI);

        // Act
        final TeacherDni result = TeacherApplicationMapper.fromGetTeacherByDniQueryToTeacherDni(query);

        // Assert
        assertEquals(DNI, result.value(), "dni debe coincidir con el del query");
    }

    // ── fromDeleteCommandToTeacherId()

    @Test
    @DisplayName("fromDeleteCommandToTeacherId() extrae el TeacherId del command")
    void shouldExtractTeacherIdFromDeleteCommand() {
        // Arrange
        final DeleteTeacherCommand command = new DeleteTeacherCommand(ID);

        // Act
        final TeacherId result = TeacherApplicationMapper.fromDeleteCommandToTeacherId(command);

        // Assert
        assertEquals(ID, result.value(), "id debe coincidir con el del command");
    }
}