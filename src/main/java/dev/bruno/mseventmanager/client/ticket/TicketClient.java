package dev.bruno.mseventmanager.client.ticket;

import dev.bruno.mseventmanager.domain.EventCheck;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ticket", url="http://localhost:9001/tickets/check-tickets-by-event")
public interface TicketClient {
    @GetMapping("/{eventId}")
    EventCheck getEventCheck(@PathVariable("eventId") String eventId);
}