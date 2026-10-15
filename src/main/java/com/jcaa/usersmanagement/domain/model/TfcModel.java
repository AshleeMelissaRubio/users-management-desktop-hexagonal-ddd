package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.TfcStatus;
import com.jcaa.usersmanagement.domain.exception.DomainException;
import com.jcaa.usersmanagement.domain.exception.InvalidTeacherAddressException;
import com.jcaa.usersmanagement.domain.exception.InvalidTeacherIdException;
import com.jcaa.usersmanagement.domain.exception.InvalidTfcStatusException;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TfcDescription;
import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import com.jcaa.usersmanagement.domain.valueobject.TfcTitle;
import lombok.Value;

@Value
public class TfcModel {

    TfcId id;
    TfcTitle title;
    TfcDescription description;
    TeacherId advisorId; // ID del profesor tutor / director
    TfcStatus status;

    // Método estático para crear un TFC en estado inicial
    public static TfcModel create(
            final TfcId id,
            final TfcTitle title,
            final TfcDescription description,
            final TeacherId advisorId) {
        return new TfcModel(id, title, description, advisorId, TfcStatus.PROPOSED);
    }

    // Regla de Negocio: Reasignar profesor tutor
    public TfcModel reassignAdvisor(final TeacherId newAdvisorId) {
        if (newAdvisorId == null) {
            throw InvalidTeacherIdException.becauseValueIsEmpty();
        }
        return new TfcModel(id, title, description, newAdvisorId, status);
    }

    // Regla de Negocio: Aprobar TFC
    public TfcModel approve() {
        if (this.status == TfcStatus.REJECTED) {
            throw InvalidTfcStatusException.becauseValueCannotTransition(this.status, TfcStatus.APPROVED);
        }
        return new TfcModel(id, title, description, advisorId, TfcStatus.APPROVED);
    }

    // Regla de Negocio: Rechazar TFC
    public TfcModel reject() {
        return new TfcModel(id, title, description, advisorId, TfcStatus.REJECTED);
    }
}
