package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.valueobject.TfcId;
import java.util.Map;
import lombok.Getter;

@Getter
public final class TfcDeletedDomainEvent extends DomainEvent {

    private static final String EVENT_NAME = "tfc.deleted";

    private final TfcId tfcId;

    public TfcDeletedDomainEvent(final TfcId tfcId) {
        super(EVENT_NAME);
        this.tfcId = tfcId;
    }

    @Override
    public Map<String, String> payload() {
        return Map.of("id", tfcId.value());
    }
}