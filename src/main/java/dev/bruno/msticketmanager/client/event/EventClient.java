package dev.bruno.msticketmanager.client.event;

import dev.bruno.msticketmanager.domain.Event;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ticket", url="http://localhost:9000/events")
public interface EventClient {
    @GetMapping("/get-event/{eventId}")
    Event getEvent(@PathVariable("eventId") String eventId);
}