package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetTeacherByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByIdQuery;
import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.exception.TeacherNotFoundException;
import com.jcaa.usersmanagement.domain.model.TeacherModel;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para GetTeacherByIdService.
 *
 * <p>Cubre: retorno del profesor encontrado, TeacherNotFoundException y validación del query.
 */
@DisplayName("GetTeacherByIdService")
@ExtendWith(MockitoExtension.class)
class GetTeacherByIdServiceTest {

    @Mock private GetTeacherByIdPort getTeacherByIdPort;

    private GetTeacherByIdService service;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            service = new GetTeacherByIdService(getTeacherByIdPort, validatorFactory.getValidator());
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() retorna el profesor cuando el id existe")
    void shouldReturnTeacherWhenFound() {
        // Arrange
        final GetTeacherByIdQuery query = new GetTeacherByIdQuery("t-001");

        final TeacherModel expected =
                new TeacherModel(
                        new TeacherId("t-001"),
                        new TeacherDni("12345678A"),
                        new TeacherName("John Arrieta"),
                        new TeacherAddress("Calle 123"),
                        TeacherStatus.ACTIVE);

        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.of(expected));

        // Act
        final TeacherModel result = service.execute(query);

        // Assert
        assertSame(expected, result, "debe retornar exactamente el profesor del puerto");
    }

    // ── profesor no encontrado

    @Test
    @DisplayName("execute() lanza TeacherNotFoundException cuando el id no existe")
    void shouldThrowWhenTeacherNotFound() {
        // Arrange
        final GetTeacherByIdQuery query = new GetTeacherByIdQuery("no-existe");

        when(getTeacherByIdPort.getById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeacherNotFoundException.class, () -> service.execute(query));
    }

    // ── validación del query

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
    void shouldThrowWhenQueryIsInvalid() {
        // Arrange
        final GetTeacherByIdQuery query = new GetTeacherByIdQuery("");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> service.execute(query));
        verifyNoInteractions(getTeacherByIdPort);
    }
}