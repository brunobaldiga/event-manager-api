package dev.bruno.msticketmanager.services;

import dev.bruno.msticketmanager.domain.Event;
import dev.bruno.msticketmanager.domain.Ticket;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;
import dev.bruno.msticketmanager.rabbitmq.EventRequestProducer;
import dev.bruno.msticketmanager.repositories.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRequestProducer eventRequestProducer;

    public TicketService(TicketRepository ticketRepository, EventRequestProducer eventRequestProducer) {
        this.ticketRepository = ticketRepository;
        this.eventRequestProducer = eventRequestProducer;
    }

    public Ticket purchaseTicket(TicketSaveRequest ticketPurchaseRequest) {
        eventRequestProducer.sendEventRequest(ticketPurchaseRequest.getEventId());

        Event event;

        try {
            event = eventRequestProducer.getEventResponse();

            if (event == null) {
                throw new IllegalArgumentException("Event not found");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException("Failed: ", e);
        }

        Ticket ticket = ticketPurchaseRequest.toModel();
        ticket.setStatus("concluído");
        ticket.setEvent(event);

        return ticketRepository.save(ticket);
    }
}
