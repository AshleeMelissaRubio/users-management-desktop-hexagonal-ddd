package com.jcaa.usersmanagement.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TeacherDeletedDomainEvent.
 *
 * <p>Verifica que el nombre del evento sea correcto, que occurredOn quede registrado, que
 * el accessor teacherId() devuelva la misma referencia y que el payload contenga únicamente un campo.
 */
@DisplayName("TeacherDeletedDomainEvent")
class TeacherDeletedDomainEventTest {

    private static final String ID = "teacher-002";

    // ── eventName

    @Test
    @DisplayName("eventName() debe retornar la constante 'teacher.deleted'")
    void shouldHaveEventNameTeacherDeleted() {
        // Arrange
        final TeacherDeletedDomainEvent event = new TeacherDeletedDomainEvent(new TeacherId(ID));

        // Act
        final String result = event.getEventName();

        // Assert
        assertEquals("teacher.deleted", result);
    }

    // ── occurredOn

    @Test
    @DisplayName("occurredOn() no debe ser nulo y debe quedar acotado al instante de construcción")
    void shouldRecordOccurredOnAtCreationTime() {
        // Arrange
        final LocalDateTime before = LocalDateTime.now();
        final TeacherDeletedDomainEvent event = new TeacherDeletedDomainEvent(new TeacherId(ID));
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

    // ── teacherId()

    @Test
    @DisplayName("getTeacherId() debe devolver la misma instancia de TeacherId recibida en el constructor")
    void shouldReturnSameTeacherIdInstance() {
        // Arrange
        final TeacherId teacherId = new TeacherId(ID);
        final TeacherDeletedDomainEvent event = new TeacherDeletedDomainEvent(teacherId);

        // Act
        final TeacherId result = event.getTeacherId();

        // Assert
        assertSame(teacherId, result);
    }

    // ── payload()

    @Test
    @DisplayName("payload() debe contener únicamente la entrada 'id' con el valor del TeacherId")
    void shouldReturnPayloadWithOnlyTeacherId() {
        // Arrange
        final TeacherDeletedDomainEvent event = new TeacherDeletedDomainEvent(new TeacherId(ID));

        // Act
        final Map<String, String> payload = event.payload();

        // Assert
        assertAll(
                "payload de TeacherDeletedDomainEvent",
                () -> assertEquals(1, payload.size(), "el mapa debe tener exactamente 1 entrada"),
                () -> assertEquals(ID, payload.get("id"), "id"));
    }
}