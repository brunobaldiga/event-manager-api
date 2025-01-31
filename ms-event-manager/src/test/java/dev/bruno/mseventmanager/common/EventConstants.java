package dev.bruno.mseventmanager.common;

import dev.bruno.mseventmanager.domain.Event;
import dev.bruno.mseventmanager.domain.representation.EventSaveRequest;

public class EventConstants {
    public static final Event EVENT_1 = new Event("1", "Feira Literária", "2024-10-05T10:00:00", "40010-000", "Avenida da França", "Comércio", "Salvador", "BA");
    public static final EventSaveRequest EVENT_1_REPRESENTATION = new EventSaveRequest("Feira Literária", "2024-10-05T10:00:00", "40010-000");

    public static final Event EVENT_2 = new Event("2", "Festival de Música", "2024-11-15T18:00:00", "50100-000", "Rua das Orquídeas", "Centro", "Rio de Janeiro", "RJ");
    public static final EventSaveRequest EVENT_2_REPRESENTATION = new EventSaveRequest("Festival de Música", "2024-11-15T18:00:00", "50100-000");

    public static final EventSaveRequest INVALID_EVENT_REPRESENTATION = new EventSaveRequest(
            "Feira Literária",
            "2024-10-05T10:00:00",
            "00000-000"
    );

    public static final EventSaveRequest EMPTY_EVENT_REPRESENTATION = new EventSaveRequest();
}
