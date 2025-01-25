package dev.bruno.mseventmanager.domain.representation;

import dev.bruno.mseventmanager.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventSaveRequest {
    private String eventName;
    private String dateTime;
    private String cep;

    public Event toModel() {
        return new Event(eventName, dateTime, cep);
    }
}
