package com.jcaa.usersmanagement.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TfcDeletedDomainEvent.
 *
 * <p>Verifica que el nombre del evento sea correcto, que occurredOn quede registrado, que
 * el accessor tfcId() devuelva la misma referencia y que el payload contenga únicamente un campo.
 */
@DisplayName("TfcDeletedDomainEvent")
class TfcDeletedDomainEventTest {

    private static final String ID = "tfc-002";

    // ── eventName

    @Test
    @DisplayName("eventName() debe retornar la constante 'tfc.deleted'")
    void shouldHaveEventNameTfcDeleted() {
        // Arrange
        final TfcDeletedDomainEvent event = new TfcDeletedDomainEvent(new TfcId(ID));

        // Act
        final String result = event.getEventName();

        // Assert
        assertEquals("tfc.deleted", result);
    }

    // ── occurredOn

    @Test
    @DisplayName("occurredOn() no debe ser nulo y debe quedar acotado al instante de construcción")
    void shouldRecordOccurredOnAtCreationTime() {
        // Arrange
        final LocalDateTime before = LocalDateTime.now();
        final TfcDeletedDomainEvent event = new TfcDeletedDomainEvent(new TfcId(ID));
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

    // ── tfcId()

    @Test
    @DisplayName("getTfcId() debe devolver la misma instancia de TfcId recibida en el constructor")
    void shouldReturnSameTfcIdInstance() {
        // Arrange
        final TfcId tfcId = new TfcId(ID);
        final TfcDeletedDomainEvent event = new TfcDeletedDomainEvent(tfcId);

        // Act
        final TfcId result = event.getTfcId();

        // Assert
        assertSame(tfcId, result);
    }

    // ── payload()

    @Test
    @DisplayName("payload() debe contener únicamente la entrada 'id' con el valor del TfcId")
    void shouldReturnPayloadWithOnlyTfcId() {
        // Arrange
        final TfcDeletedDomainEvent event = new TfcDeletedDomainEvent(new TfcId(ID));

        // Act
        final Map<String, String> payload = event.payload();

        // Assert
        assertAll(
                "payload de TfcDeletedDomainEvent",
                () -> assertEquals(1, payload.size(), "el mapa debe tener exactamente 1 entrada"),
                () -> assertEquals(ID, payload.get("id"), "id"));
    }
}