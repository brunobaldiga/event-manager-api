package dev.bruno.msticketmanager.services;

import dev.bruno.msticketmanager.client.event.EventClient;
import dev.bruno.msticketmanager.domain.Event;
import dev.bruno.msticketmanager.domain.EventCheck;
import dev.bruno.msticketmanager.domain.Ticket;
import dev.bruno.msticketmanager.domain.mapper.TicketMapper;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;
import dev.bruno.msticketmanager.exceptions.ResourceNotFoundException;
import dev.bruno.msticketmanager.repositories.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventClient eventClient;

    public TicketService(TicketRepository ticketRepository, EventClient eventClient) {
        this.ticketRepository = ticketRepository;
        this.eventClient = eventClient;
    }

    public Ticket purchaseTicket(TicketSaveRequest ticketPurchaseRequest) {
        Event event = getEvent(ticketPurchaseRequest.getEventId());

        Ticket ticket = ticketPurchaseRequest.toModel();
        ticket.setStatus("concluído");
        ticket = ticketRepository.save(ticket);
        ticket.setEvent(event);

        return ticket;
    }

    public Ticket findById(String id) {
        Ticket ticket = getTicket(id);

        ticket.setEvent(getEvent(ticket.getEventId()));

        return ticket;
    }

    public Ticket update(String id, TicketSaveRequest ticketSaveRequest) {
        Ticket ticket = getTicket(id);

        TicketMapper.update(ticketSaveRequest, ticket);

        ticket = ticketRepository.save(ticket);
        ticket.setEvent(getEvent(ticketSaveRequest.getEventId()));

        return ticket;
    }

    private Ticket getTicket(String id) {
        return ticketRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ticket não encontrado.")
        );
    }

    public void softDelete(String id) {
        Ticket ticket = getTicket(id);

        ticket.setActive(false);

        ticketRepository.save(ticket);
    }

    public EventCheck checkTicketsByEventId(String eventId) {
        return new EventCheck(eventId, ticketRepository.existsByEventIdAndActiveTrue(eventId));
    }

    private Event getEvent(String eventId) {
        return eventClient.getEvent(eventId);
    }
}
