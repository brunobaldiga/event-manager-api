package dev.bruno.msticketmanager.services;

import dev.bruno.msticketmanager.client.event.EventClient;
import dev.bruno.msticketmanager.domain.Email;
import dev.bruno.msticketmanager.domain.Event;
import dev.bruno.msticketmanager.domain.EventCheck;
import dev.bruno.msticketmanager.domain.Ticket;
import dev.bruno.msticketmanager.domain.mapper.TicketMapper;
import dev.bruno.msticketmanager.domain.representation.EmailTemplate;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;
import dev.bruno.msticketmanager.exceptions.ResourceNotFoundException;
import dev.bruno.msticketmanager.rabbitmq.EmailPublisher;
import dev.bruno.msticketmanager.repositories.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventClient eventClient;
    private final EmailPublisher emailPublisher;


    public TicketService(TicketRepository ticketRepository, EventClient eventClient, EmailPublisher emailPublisher) {
        this.ticketRepository = ticketRepository;
        this.eventClient = eventClient;
        this.emailPublisher = emailPublisher;
    }

    public Ticket purchaseTicket(TicketSaveRequest ticketPurchaseRequest) {
        Event event = getEvent(ticketPurchaseRequest.getEventId());

        Ticket ticket = ticketPurchaseRequest.toModel();
        ticket.setStatus("concluído");
        ticket = ticketRepository.save(ticket);
        ticket.setEvent(event);

        String template = new EmailTemplate(
                ticket.getCustomerName(),
                ticket.getEvent().getEventName(),
                LocalDateTime.parse(
                        ticket.getEvent().getDate()
                ).format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
                ),
                ticket.getId()
        ).generateEmailBody();

        emailPublisher.sendEmailToQueue(
                new Email(
                        ticket.getCustomerMail(),
                        "Confirmação da compra do ingresso",
                        template
                )
        );

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

    public void softDelete(String id) {
        Ticket ticket = getTicket(id);

        ticket.setActive(false);

        ticketRepository.save(ticket);
    }

    public EventCheck checkTicketsByEventId(String eventId) {
        return new EventCheck(eventId, ticketRepository.existsByEventIdAndActiveTrue(eventId));
    }

    private Ticket getTicket(String id) {
        return ticketRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ticket não encontrado.")
        );
    }

    private Event getEvent(String eventId) {
        return eventClient.getEvent(eventId);
    }
}
