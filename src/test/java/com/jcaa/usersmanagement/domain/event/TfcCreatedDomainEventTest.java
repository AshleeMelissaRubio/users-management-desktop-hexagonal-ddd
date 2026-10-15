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
 * Tests para TfcCreatedDomainEvent.
 *
 * <p>Estrategia: un @Test por cada comportamiento observable del evento (eventName, occurredOn,
 * tfc, payload).
 */
@DisplayName("Test de TfcCreatedDomainEvent")
class TfcCreatedDomainEventTest {

    // ── Arrange Generales
    private static final String ID = "tfc-001";
    private static final String TITLE = "Sistema de Gestión";
    private static final String DESCRIPTION = "Descripción del proyecto";
    private static final String ADVISOR_ID = "t-001";

    private TfcModel tfc;

    @BeforeEach
    void setUp() {
        tfc =
                new TfcModel(
                        new TfcId(ID),
                        new TfcTitle(TITLE),
                        new TfcDescription(DESCRIPTION),
                        new TeacherId(ADVISOR_ID),
                        TfcStatus.PROPOSED);
    }

    // ── eventName

    @Test
    @DisplayName("eventName() debe retornar la constante 'tfc.created'")
    void shouldHaveEventNameTfcCreated() {
        // Arrange
        final TfcCreatedDomainEvent event = new TfcCreatedDomainEvent(tfc);

        // Act
        final String result = event.getEventName();

        // Assert
        assertEquals("tfc.created", result);
    }

    // ── occurredOn

    @Test
    @DisplayName("occurredOn() no debe ser nulo y debe quedar acotado al instante de construcción")
    void shouldRecordOccurredOnAtCreationTime() {
        // Arrange
        final LocalDateTime before = LocalDateTime.now();
        final TfcCreatedDomainEvent event = new TfcCreatedDomainEvent(tfc);
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
        final TfcCreatedDomainEvent event = new TfcCreatedDomainEvent(tfc);

        // Act
        final TfcModel result = event.getTfc();

        // Assert
        assertSame(tfc, result);
    }

    // ── payload()

    @Test
    @DisplayName("payload() debe contener exactamente los cinco campos del TFC")
    void shouldReturnPayloadWithAllTfcFields() {
        // Arrange
        final TfcCreatedDomainEvent event = new TfcCreatedDomainEvent(tfc);

        // Act
        final Map<String, String> payload = event.payload();

        // Assert
        assertAll(
                "payload de TfcCreatedDomainEvent",
                () -> assertEquals(5, payload.size(), "tamaño del mapa"),
                () -> assertEquals(ID, payload.get("id"), "id"),
                () -> assertEquals(TITLE, payload.get("title"), "title"),
                () -> assertEquals(DESCRIPTION, payload.get("description"), "description"),
                () -> assertEquals(ADVISOR_ID, payload.get("advisorId"), "advisorId"),
                () -> assertEquals(TfcStatus.PROPOSED.name(), payload.get("status"), "status"));
    }
}