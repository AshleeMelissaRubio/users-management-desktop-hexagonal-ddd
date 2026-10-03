package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.model.TfcModel;
import java.util.Map;
import lombok.Getter;

@Getter
public final class TfcUpdatedDomainEvent extends DomainEvent {

    private static final String EVENT_NAME = "tfc.updated";

    private final TfcModel tfc;

    public TfcUpdatedDomainEvent(final TfcModel tfc) {
        super(EVENT_NAME);
        this.tfc = tfc;
    }

    @Override
    public Map<String, String> payload() {
        return Map.of(
                "id", tfc.getId().value(),
                "title", tfc.getTitle().value(),
                "advisorId", tfc.getAdvisorId().value(),
                "status", tfc.getStatus().name());
    }
}