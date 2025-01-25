package dev.bruno.msticketmanager.services;

import dev.bruno.msticketmanager.domain.Event;
import dev.bruno.msticketmanager.domain.Ticket;
import dev.bruno.msticketmanager.domain.mapper.TicketMapper;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;
import dev.bruno.msticketmanager.rabbitmq.EventRequestProducer;
import dev.bruno.msticketmanager.repositories.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRequestProducer eventRequestProducer;

    public TicketService(TicketRepository ticketRepository, EventRequestProducer eventRequestProducer) {
        this.ticketRepository = ticketRepository;
        this.eventRequestProducer = eventRequestProducer;
    }

    public Ticket purchaseTicket(TicketSaveRequest ticketPurchaseRequest) {
        Event event = getEvent(ticketPurchaseRequest);

        Ticket ticket = ticketPurchaseRequest.toModel();
        ticket.setStatus("concluído");
        ticket.setEvent(event);

        return ticketRepository.save(ticket);
    }

    public Ticket findById(String id) {
        return ticketRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Não implementado")
        );
    }

    public Ticket update(String id, TicketSaveRequest ticketSaveRequest) {
        Ticket ticket = findById(id);
        Event event = getEvent(ticketSaveRequest);

        TicketMapper.update(ticketSaveRequest, ticket);
        ticket.setEvent(event);

        return ticketRepository.save(ticket);
    }

    private Event getEvent(TicketSaveRequest ticketRequest) {
        eventRequestProducer.sendEventRequest(ticketRequest.getEventId());

        Event event;

        try {
            event = eventRequestProducer.getEventResponse();

            if (event == null) {
                throw new IllegalArgumentException("Event not found");
            }

        } catch (InterruptedException e) {
            throw new RuntimeException("Failed: ", e);
        }
        return event;
    }

    public void softDelete(String id) {
        Ticket ticket = findById(id);

        ticket.setActive(false);

        ticketRepository.save(ticket);
    }

    public List<Ticket> findByEventId(String eventId) {
        return ticketRepository.findByEventEventIdAndActiveTrue(eventId);
    }
}
