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
import com.jcaa.usersmanagement.application.service.dto.command.CreateTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateTfcCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetTfcByIdQuery;
import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.exception.TfcAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.TfcNotFoundException;
import com.jcaa.usersmanagement.domain.model.TfcModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TfcDescription;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.domain.valueobject.TfcTitle;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateTfcRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.TfcResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateTfcRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for TfcController.
 *
 * <p>Covers: correct delegation to every use-case port, accurate DTO→command/query mapping,
 * accurate domain-model→response mapping, and transparent exception propagation. All ports are
 * mocked; no infrastructure is exercised.
 */
@DisplayName("TfcController")
@ExtendWith(MockitoExtension.class)
class TfcControllerTest {

    @Mock private CreateTfcUseCase createTfcUseCase;
    @Mock private UpdateTfcUseCase updateTfcUseCase;
    @Mock private DeleteTfcUseCase deleteTfcUseCase;
    @Mock private GetTfcByIdUseCase getTfcByIdUseCase;
    @Mock private GetAllTfcsUseCase getAllTfcsUseCase;

    private TfcController controller;

    // ── helpers

    private static TfcModel buildTfc(
            final String id,
            final String title,
            final String description,
            final String advisorId,
            final TfcStatus status) {
        return new TfcModel(
                new TfcId(id),
                new TfcTitle(title),
                new TfcDescription(description),
                new TeacherId(advisorId),
                status);
    }

    @BeforeEach
    void setUp() {
        controller =
                new TfcController(
                        createTfcUseCase,
                        updateTfcUseCase,
                        deleteTfcUseCase,
                        getTfcByIdUseCase,
                        getAllTfcsUseCase);
    }

    // ── listAllTfcs

    @Test
    @DisplayName(
            "listAllTfcs() returns a correctly mapped TfcResponse list when the use case returns TFCs")
    void listAllTfcs_returnsMappedResponseList_whenTfcsExist() {
        // Arrange
        final TfcModel tfc =
                buildTfc(
                        "tfc-001",
                        "Sistema de Gestión",
                        "Descripción del proyecto",
                        "t-001",
                        TfcStatus.PROPOSED);
        when(getAllTfcsUseCase.execute()).thenReturn(List.of(tfc));

        // Act
        final List<TfcResponse> result = controller.listAllTfcs();

        // Assert
        assertAll(
                "single-TFC list mapping",
                () -> assertEquals(1, result.size(), "list must contain exactly one element"),
                () -> assertEquals("tfc-001", result.get(0).id(), "id must match"),
                () -> assertEquals("Sistema de Gestión", result.get(0).title(), "title must match"),
                () ->
                        assertEquals(
                                "Descripción del proyecto", result.get(0).description(), "description must match"),
                () -> assertEquals("t-001", result.get(0).advisorId(), "advisorId must match"),
                () -> assertEquals("PROPOSED", result.get(0).status(), "status must match enum name"));
        verify(getAllTfcsUseCase).execute();
    }

    @Test
    @DisplayName("listAllTfcs() returns an empty list when the use case returns no TFCs")
    void listAllTfcs_returnsEmptyList_whenNoTfcsExist() {
        // Arrange
        when(getAllTfcsUseCase.execute()).thenReturn(List.of());

        // Act
        final List<TfcResponse> result = controller.listAllTfcs();

        // Assert
        assertTrue(result.isEmpty(), "result must be an empty list");
        verify(getAllTfcsUseCase).execute();
    }

    // ── findTfcById

    @Test
    @DisplayName(
            "findTfcById() builds a GetTfcByIdQuery with the given id and returns the mapped response")
    void findTfcById_returnsMappedResponse_whenTfcExists() {
        // Arrange
        final TfcModel tfc =
                buildTfc(
                        "tfc-002",
                        "Plataforma Web",
                        "Desarrollo con arquitectura limpia",
                        "t-002",
                        TfcStatus.APPROVED);
        when(getTfcByIdUseCase.execute(new GetTfcByIdQuery("tfc-002"))).thenReturn(tfc);

        // Act
        final TfcResponse result = controller.findTfcById("tfc-002");

        // Assert
        assertAll(
                "findTfcById response mapping",
                () -> assertEquals("tfc-002", result.id(), "id must match"),
                () -> assertEquals("Plataforma Web", result.title(), "title must match"),
                () ->
                        assertEquals(
                                "Desarrollo con arquitectura limpia",
                                result.description(),
                                "description must match"),
                () -> assertEquals("t-002", result.advisorId(), "advisorId must match"),
                () -> assertEquals("APPROVED", result.status(), "status must match enum name"));
    }

    @Test
    @DisplayName(
            "findTfcById() propagates TfcNotFoundException when the use case cannot find the TFC")
    void findTfcById_propagatesTfcNotFoundException_whenTfcDoesNotExist() {
        // Arrange
        when(getTfcByIdUseCase.execute(new GetTfcByIdQuery("tfc-999")))
                .thenThrow(TfcNotFoundException.becauseIdWasNotFound("tfc-999"));

        // Act & Assert
        assertThrows(
                TfcNotFoundException.class,
                () -> controller.findTfcById("tfc-999"),
                "TfcNotFoundException must propagate without being wrapped");
    }

    // ── createTfc

    @Test
    @DisplayName(
            "createTfc() delegates a correctly populated CreateTfcCommand and returns the mapped response")
    void createTfc_delegatesCorrectCommandAndReturnsMappedResponse_whenCreationSucceeds() {
        // Arrange
        final CreateTfcRequest request =
                new CreateTfcRequest("tfc-003", "App Móvil", "Aplicación móvil para Android/iOS", "t-003");
        final TfcModel createdTfc =
                buildTfc(
                        "tfc-003",
                        "App Móvil",
                        "Aplicación móvil para Android/iOS",
                        "t-003",
                        TfcStatus.PROPOSED);
        final ArgumentCaptor<CreateTfcCommand> captor =
                ArgumentCaptor.forClass(CreateTfcCommand.class);
        when(createTfcUseCase.execute(captor.capture())).thenReturn(createdTfc);

        // Act
        final TfcResponse result = controller.createTfc(request);

        // Assert
        assertAll(
                "createTfc command delegation and response mapping",
                () -> assertEquals("tfc-003", captor.getValue().id(), "command id must match request id"),
                () ->
                        assertEquals(
                                "App Móvil", captor.getValue().title(), "command title must match request title"),
                () ->
                        assertEquals(
                                "Aplicación móvil para Android/iOS",
                                captor.getValue().description(),
                                "command description must match request description"),
                () ->
                        assertEquals(
                                "t-003",
                                captor.getValue().advisorId(),
                                "command advisorId must match request advisorId"),
                () ->
                        assertEquals(
                                "tfc-003",
                                result.id(),
                                "response id must come from the domain model returned by use case"),
                () ->
                        assertEquals(
                                "PROPOSED",
                                result.status(),
                                "response status must reflect the domain model status"));
    }

    @Test
    @DisplayName(
            "createTfc() propagates TfcAlreadyExistsException when the use case rejects duplicate ID")
    void createTfc_propagatesTfcAlreadyExistsException_whenIdIsDuplicated() {
        // Arrange
        final CreateTfcRequest request =
                new CreateTfcRequest("tfc-001", "Título Duplicado", "Descripción", "t-001");
        when(createTfcUseCase.execute(any()))
                .thenThrow(TfcAlreadyExistsException.becauseTitleAlreadyExists("tfc-001"));

        // Act & Assert
        assertThrows(
                TfcAlreadyExistsException.class,
                () -> controller.createTfc(request),
                "TfcAlreadyExistsException must propagate without being wrapped");
    }

    // ── updateTfc

    @Test
    @DisplayName(
            "updateTfc() delegates a correctly populated UpdateTfcCommand and returns the mapped response")
    void updateTfc_delegatesCorrectCommandAndReturnsMappedResponse_whenUpdateSucceeds() {
        // Arrange
        final UpdateTfcRequest request =
                new UpdateTfcRequest(
                        "tfc-004", "Actualizado Title", "Descripción Actualizada", "t-004", "APPROVED");
        final TfcModel updatedTfc =
                buildTfc(
                        "tfc-004",
                        "Actualizado Title",
                        "Descripción Actualizada",
                        "t-004",
                        TfcStatus.APPROVED);
        final ArgumentCaptor<UpdateTfcCommand> captor =
                ArgumentCaptor.forClass(UpdateTfcCommand.class);
        when(updateTfcUseCase.execute(captor.capture())).thenReturn(updatedTfc);

        // Act
        final TfcResponse result = controller.updateTfc(request);

        // Assert
        assertAll(
                "updateTfc command delegation and response mapping",
                () -> assertEquals("tfc-004", captor.getValue().id(), "command id must match request id"),
                () ->
                        assertEquals(
                                "Actualizado Title",
                                captor.getValue().title(),
                                "command title must match request title"),
                () ->
                        assertEquals(
                                "Descripción Actualizada",
                                captor.getValue().description(),
                                "command description must match request description"),
                () ->
                        assertEquals(
                                "t-004",
                                captor.getValue().advisorId(),
                                "command advisorId must match request advisorId"),
                () ->
                        assertEquals(
                                "tfc-004",
                                result.id(),
                                "response id must come from the domain model returned by use case"),
                () ->
                        assertEquals(
                                "APPROVED",
                                result.status(),
                                "response status must reflect the domain model status"));
    }

    @Test
    @DisplayName(
            "updateTfc() propagates TfcNotFoundException when the use case cannot find the TFC")
    void updateTfc_propagatesTfcNotFoundException_whenTfcDoesNotExist() {
        // Arrange
        final UpdateTfcRequest request =
                new UpdateTfcRequest("tfc-999", "Título Fantasma", "Descripción", "t-999", "APPROVED");
        when(updateTfcUseCase.execute(any()))
                .thenThrow(TfcNotFoundException.becauseIdWasNotFound("tfc-999"));

        // Act & Assert
        assertThrows(
                TfcNotFoundException.class,
                () -> controller.updateTfc(request),
                "TfcNotFoundException must propagate without being wrapped");
    }

    // ── deleteTfc

    @Test
    @DisplayName("deleteTfc() delegates a DeleteTfcCommand with the given id to the use case")
    void deleteTfc_delegatesDeleteCommandWithCorrectId() {
        // Arrange
        final ArgumentCaptor<DeleteTfcCommand> captor =
                ArgumentCaptor.forClass(DeleteTfcCommand.class);
        doNothing().when(deleteTfcUseCase).execute(captor.capture());

        // Act
        controller.deleteTfc("tfc-005");

        // Assert
        assertEquals("tfc-005", captor.getValue().id(), "delete command id must match the provided id");
    }

    @Test
    @DisplayName(
            "deleteTfc() propagates TfcNotFoundException when the use case cannot find the TFC")
    void deleteTfc_propagatesTfcNotFoundException_whenTfcDoesNotExist() {
        // Arrange
        doThrow(TfcNotFoundException.becauseIdWasNotFound("tfc-999"))
                .when(deleteTfcUseCase)
                .execute(any());

        // Act & Assert
        assertThrows(
                TfcNotFoundException.class,
                () -> controller.deleteTfc("tfc-999"),
                "TfcNotFoundException must propagate without being wrapped");
    }
}