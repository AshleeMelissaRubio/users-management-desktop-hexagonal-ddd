package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.TeacherStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidTeacherAddressException;
import com.jcaa.usersmanagement.domain.valueobject.TeacherAddress;
import com.jcaa.usersmanagement.domain.valueobject.TeacherDni;
import com.jcaa.usersmanagement.domain.valueobject.TeacherId;
import com.jcaa.usersmanagement.domain.valueobject.TeacherName;
import lombok.Value;

@Value
public class TeacherModel {

    TeacherId id;
    TeacherDni dni;
    TeacherName name;
    TeacherAddress address;
    TeacherStatus status;

    public static TeacherModel create(
            final TeacherId id,
            final TeacherDni dni,
            final TeacherName name,
            final TeacherAddress address) {
        return new TeacherModel(id, dni, name, address, TeacherStatus.ACTIVE);
    }

    // Actualización inmutable de dirección (retorna un NUEVO TeacherModel)
    public TeacherModel updateAddress(final TeacherAddress newAddress) {
        if (newAddress == null) {
            throw InvalidTeacherAddressException.becauseValueIsEmpty();
        }
        if (this.status == TeacherStatus.INACTIVE) {
            throw InvalidTeacherAddressException.becauseTeacherIsInactive();
        }
        return new TeacherModel(id, dni, name, newAddress, status);
    }

    public TeacherModel activate() {
        return new TeacherModel(id, dni, name, address, TeacherStatus.ACTIVE);
    }

    public TeacherModel deactivate() {
        return new TeacherModel(id, dni, name, address, TeacherStatus.INACTIVE);
    }

}
