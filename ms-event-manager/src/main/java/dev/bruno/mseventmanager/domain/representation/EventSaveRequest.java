package dev.bruno.mseventmanager.domain.representation;

import dev.bruno.mseventmanager.domain.Event;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventSaveRequest {
    @NotBlank
    private String eventName;

    @NotBlank
    private String dateTime;

    @NotBlank
    private String cep;

    public Event toModel() {
        return new Event(eventName, dateTime, cep);
    }
}
