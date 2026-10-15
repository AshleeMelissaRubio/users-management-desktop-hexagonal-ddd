package com.jcaa.usersmanagement.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TeacherUpdatedDomainEvent.
 *
 * <p>Misma estructura que TeacherCreatedDomainEvent pero con el nombre de evento "teacher.updated".
 */
@DisplayName("TeacherUpdatedDomainEvent")
class TeacherUpdatedDomainEventTest {

    // ── Arranges globales
    private static final String ID = "teacher-003";
    private static final String DNI = "87654321B";
    private static final String NAME = "Jane Doe";
    private static final String ADDRESS = "Avenida 456";

    private TeacherModel teacher;

    @BeforeEach
    void setUp() {
        teacher =
                new TeacherModel(
                        new TeacherId(ID),
                        new TeacherDni(DNI),
                        new TeacherName(NAME),
                        new TeacherAddress(ADDRESS),
                        TeacherStatus.INACTIVE);
    }

    // ── eventName

    @Test
    @DisplayName("eventName() debe retornar la constante 'teacher.updated'")
    void shouldHaveEventNameTeacherUpdated() {
        // Arrange
        final TeacherUpdatedDomainEvent event = new TeacherUpdatedDomainEvent(teacher);

        // Act
        final String result = event.getEventName();

        // Assert
        assertEquals("teacher.updated", result);
    }

    // ── occurredOn

    @Test
    @DisplayName("occurredOn() no debe ser nulo y debe quedar acotado al instante de construcción")
    void shouldRecordOccurredOnAtCreationTime() {
        // Arrange
        final LocalDateTime before = LocalDateTime.now();
        final TeacherUpdatedDomainEvent event = new TeacherUpdatedDomainEvent(teacher);
        final LocalDateTime after = LocalDateTime.now();

        // Act
        final LocalDateTime occurredOn = event.getOccurredOn();

        // Assert
        assertNotNull(occurredOn, "occurredOn no debe ser null");
        assertFalse(
                occurredOn.isBefore(before),
                "occurredOn debe ser >= al instante anterior a la construcción");
        assertFalse(
                occurredOn.isAfter(after),
                "occurredOn debe ser <= al instante posterior a la construcción");
    }

    // ── teacher()

    @Test
    @DisplayName("getTeacher() debe devolver la misma instancia de TeacherModel recibida en el constructor")
    void shouldReturnSameTeacherInstance() {
        // Arrange
        final TeacherUpdatedDomainEvent event = new TeacherUpdatedDomainEvent(teacher);

        // Act
        final TeacherModel result = event.getTeacher();

        // Assert
        assertSame(teacher, result);
    }

    // ── payload()

    @Test
    @DisplayName("payload() debe contener exactamente los cinco campos del profesor actualizado")
    void shouldReturnPayloadWithAllTeacherFields() {
        // Arrange
        final TeacherUpdatedDomainEvent event = new TeacherUpdatedDomainEvent(teacher);

        // Act
        final Map<String, String> payload = event.payload();

        // Assert
        assertAll(
                "payload de TeacherUpdatedDomainEvent",
                () -> assertEquals(5, payload.size(), "tamaño del mapa"),
                () -> assertEquals(ID, payload.get("id"), "id"),
                () -> assertEquals(DNI, payload.get("dni"), "dni"),
                () -> assertEquals(NAME, payload.get("name"), "name"),
                () -> assertEquals(ADDRESS, payload.get("address"), "address"),
                () -> assertEquals(TeacherStatus.INACTIVE.name(), payload.get("status"), "status"));
    }
}