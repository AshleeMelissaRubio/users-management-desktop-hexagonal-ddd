package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TeacherModel.
 *
 * <p>Sólo se testean los tres métodos con lógica real: {@code create()} que fija el estado inicial
 * PENDING, {@code activate()} y {@code deactivate()} que producen transiciones de estado. El
 * constructor trivial y los getters quedan cubiertos como efecto secundario de estos tests.
 */
@DisplayName("TeacherModel")
class TeacherModelTest {

    // ── Arrange (variables globales para la mayoría de las test)
    private TeacherId teacherId;
    private TeacherDni teacherDni;
    private TeacherName teacherName;
    private TeacherAddress teacherAddress;

    @BeforeEach
    void setUp() {
        teacherId = new TeacherId("t-001");
        teacherDni = new TeacherDni("12345678A");
        teacherName = new TeacherName("John Arrieta");
        teacherAddress = new TeacherAddress("Calle 123");
    }

    // ── create()

    @Test
    @DisplayName("create() debe fijar status PENDING y preservar todos los campos recibidos")
    void shouldCreateTeacherWithPendingStatusAndPreserveAllFields() {
        // Act
        final TeacherModel model =
                TeacherModel.create(teacherId, teacherDni, teacherName, teacherAddress);

        // Assert
        assertAll(
                "create() factory",
                () ->
                        assertEquals(
                                TeacherStatus.PENDING, model.getStatus(), "status debe iniciar como PENDING"),
                () -> assertSame(teacherId, model.getId(), "id debe preservarse"),
                () -> assertSame(teacherDni, model.getDni(), "dni debe preservarse"),
                () -> assertSame(teacherName, model.getName(), "name debe preservarse"),
                () -> assertSame(teacherAddress, model.getAddress(), "address debe preservarse"));
    }

    // ── activate()

    @Test
    @DisplayName("activate() debe retornar nueva instancia con ACTIVE y demás campos intactos")
    void shouldActivateAndPreserveOtherFields() {
        // Arrange
        final TeacherModel pending =
                TeacherModel.create(teacherId, teacherDni, teacherName, teacherAddress);

        // Act
        final TeacherModel activated = pending.activate();

        // Assert
        assertAll(
                "resultado de activate()",
                () -> assertNotSame(pending, activated, "debe ser una nueva instancia"),
                () -> assertEquals(TeacherStatus.ACTIVE, activated.getStatus(), "status debe ser ACTIVE"),
                () -> assertSame(teacherId, activated.getId(), "id debe preservarse"),
                () -> assertSame(teacherDni, activated.getDni(), "dni debe preservarse"),
                () -> assertSame(teacherName, activated.getName(), "name debe preservarse"),
                () -> assertSame(teacherAddress, activated.getAddress(), "address debe preservarse"));
    }

    // ── deactivate()

    @Test
    @DisplayName("deactivate() debe retornar nueva instancia con INACTIVE y demás campos intactos")
    void shouldDeactivateAndPreserveOtherFields() {
        // Arrange
        final TeacherModel active =
                new TeacherModel(teacherId, teacherDni, teacherName, teacherAddress, TeacherStatus.ACTIVE);

        // Act
        final TeacherModel deactivated = active.deactivate();

        // Assert
        assertAll(
                "resultado de deactivate()",
                () -> assertNotSame(active, deactivated, "debe ser una nueva instancia"),
                () ->
                        assertEquals(
                                TeacherStatus.INACTIVE, deactivated.getStatus(), "status debe ser INACTIVE"),
                () -> assertSame(teacherId, deactivated.getId(), "id debe preservarse"),
                () -> assertSame(teacherDni, deactivated.getDni(), "dni debe preservarse"),
                () -> assertSame(teacherName, deactivated.getName(), "name debe preservarse"),
                () -> assertSame(teacherAddress, deactivated.getAddress(), "address debe preservarse"));
    }
}