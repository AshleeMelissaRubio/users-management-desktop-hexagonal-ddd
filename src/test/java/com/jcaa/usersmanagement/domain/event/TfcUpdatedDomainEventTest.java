package com.jcaa.usersmanagement.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TfcDescription;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.domain.valueobject.TfcTitle;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests para TfcUpdatedDomainEvent.
 *
 * <p>Misma estructura que TfcCreatedDomainEvent pero con el nombre de evento "tfc.updated".
 */
@DisplayName("TfcUpdatedDomainEvent")
class TfcUpdatedDomainEventTest {

    // ── Arranges globales
    private static final String ID = "tfc-003";
    private static final String TITLE = "Sistema de Gestión V2";
    private static final String DESCRIPTION = "Nueva descripción actualizada";
    private static final String ADVISOR_ID = "t-002";

    private TfcModel tfc;

    @BeforeEach
    void setUp() {
        tfc =
                new TfcModel(
                        new TfcId(ID),
                        new TfcTitle(TITLE),
                        new TfcDescription(DESCRIPTION),
                        new TeacherId(ADVISOR_ID),
                        TfcStatus.APPROVED);
    }

    // ── eventName

    @Test
    @DisplayName("eventName() debe retornar la constante 'tfc.updated'")
    void shouldHaveEventNameTfcUpdated() {
        // Arrange
        final TfcUpdatedDomainEvent event = new TfcUpdatedDomainEvent(tfc);

        // Act
        final String result = event.getEventName();

        // Assert
        assertEquals("tfc.updated", result);
    }

    // ── occurredOn

    @Test
    @DisplayName("occurredOn() no debe ser nulo y debe quedar acotado al instante de construcción")
    void shouldRecordOccurredOnAtCreationTime() {
        // Arrange
        final LocalDateTime before = LocalDateTime.now();
        final TfcUpdatedDomainEvent event = new TfcUpdatedDomainEvent(tfc);
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

    // ── tfc()

    @Test
    @DisplayName("getTfc() debe devolver la misma instancia de TfcModel recibida en el constructor")
    void shouldReturnSameTfcInstance() {
        // Arrange
        final TfcUpdatedDomainEvent event = new TfcUpdatedDomainEvent(tfc);

        // Act
        final TfcModel result = event.getTfc();

        // Assert
        assertSame(tfc, result);
    }

    // ── payload()

    @Test
    @DisplayName("payload() debe contener exactamente los cinco campos del TFC actualizado")
    void shouldReturnPayloadWithAllTfcFields() {
        // Arrange
        final TfcUpdatedDomainEvent event = new TfcUpdatedDomainEvent(tfc);

        // Act
        final Map<String, String> payload = event.payload();

        // Assert
        assertAll(
                "payload de TfcUpdatedDomainEvent",
                () -> assertEquals(5, payload.size(), "tamaño del mapa"),
                () -> assertEquals(ID, payload.get("id"), "id"),
                () -> assertEquals(TITLE, payload.get("title"), "title"),
                () -> assertEquals(DESCRIPTION, payload.get("description"), "description"),
                () -> assertEquals(ADVISOR_ID, payload.get("advisorId"), "advisorId"),
                () -> assertEquals(TfcStatus.APPROVED.name(), payload.get("status"), "status"));
    }
}