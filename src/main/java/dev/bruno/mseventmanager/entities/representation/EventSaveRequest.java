package dev.bruno.mseventmanager.entities.representation;

import dev.bruno.mseventmanager.entities.Event;
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
