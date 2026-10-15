package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetTeacherByDniPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetTeacherByDniQuery;
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
 * Tests para GetTeacherByDniService.
 *
 * <p>Cubre: retorno del profesor encontrado por DNI, TeacherNotFoundException y validación del query.
 */
@DisplayName("GetTeacherByDniService")
@ExtendWith(MockitoExtension.class)
class GetTeacherByDniServiceTest {

    @Mock private GetTeacherByDniPort getTeacherByDniPort;

    private GetTeacherByDniService service;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            service = new GetTeacherByDniService(getTeacherByDniPort, validatorFactory.getValidator());
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() retorna el profesor cuando el DNI existe")
    void shouldReturnTeacherWhenFoundByDni() {
        // Arrange
        final GetTeacherByDniQuery query = new GetTeacherByDniQuery("12345678A");

        final TeacherModel expected =
                new TeacherModel(
                        new TeacherId("t-001"),
                        new TeacherDni("12345678A"),
                        new TeacherName("John Arrieta"),
                        new TeacherAddress("Calle 123"),
                        TeacherStatus.ACTIVE);

        when(getTeacherByDniPort.getByDni(any())).thenReturn(Optional.of(expected));

        // Act
        final TeacherModel result = service.execute(query);

        // Assert
        assertSame(expected, result, "debe retornar exactamente el profesor del puerto");
    }

    // ── profesor no encontrado

    @Test
    @DisplayName("execute() lanza TeacherNotFoundException cuando el DNI no existe")
    void shouldThrowWhenTeacherNotFoundByDni() {
        // Arrange
        final GetTeacherByDniQuery query = new GetTeacherByDniQuery("99999999Z");

        when(getTeacherByDniPort.getByDni(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeacherNotFoundException.class, () -> service.execute(query));
    }

    // ── validación del query

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando el DNI está en blanco o es inválido")
    void shouldThrowWhenQueryIsInvalid() {
        // Arrange
        final GetTeacherByDniQuery query = new GetTeacherByDniQuery("");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> service.execute(query));
        verifyNoInteractions(getTeacherByDniPort);
    }
}