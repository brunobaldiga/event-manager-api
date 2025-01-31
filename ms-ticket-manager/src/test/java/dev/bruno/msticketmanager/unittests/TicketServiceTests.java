package dev.bruno.msticketmanager.unittests;

import dev.bruno.msticketmanager.client.event.EventClient;
import dev.bruno.msticketmanager.domain.Ticket;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;
import dev.bruno.msticketmanager.exceptions.ResourceNotFoundException;
import dev.bruno.msticketmanager.rabbitmq.EmailConsumer;
import dev.bruno.msticketmanager.rabbitmq.EmailPublisher;
import dev.bruno.msticketmanager.repositories.TicketRepository;
import dev.bruno.msticketmanager.services.TicketService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dev.bruno.msticketmanager.common.EventConstants.EVENT_1;
import static dev.bruno.msticketmanager.common.EventConstants.EVENT_2;
import static dev.bruno.msticketmanager.common.TicketConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTests {
    @InjectMocks
    private TicketService service;

    @Mock
    TicketRepository repository;

    @Mock
    EventClient eventClient;

    @Mock
    EmailConsumer emailConsumer;

    @Mock
    EmailPublisher emailPublisher;


    @Test
    public void createTicket_WithValidData_ReturnsEvent() {
        when(eventClient.getEvent(EVENT_1.getEventId())).thenReturn(EVENT_1);
        when(repository.save(any(Ticket.class))).thenReturn(TICKET_1);

        Ticket ticket = service.purchaseTicket(TICKET_1_REPRESENTATION);

        Assertions.assertThat(ticket.getCpf()).isEqualTo(TICKET_1.getCpf());
        Assertions.assertThat(ticket.getCustomerName()).isEqualTo(TICKET_1.getCustomerName());
        Assertions.assertThat(ticket.getCustomerMail()).isEqualTo(TICKET_1.getCustomerMail());
        Assertions.assertThat(ticket.getBRLamount()).isEqualTo(TICKET_1.getBRLamount());
        Assertions.assertThat(ticket.getUSDamount()).isEqualTo(TICKET_1.getUSDamount());
        Assertions.assertThat(ticket.getStatus()).isEqualTo(TICKET_1.getStatus());
        Assertions.assertThat(ticket.getActive()).isEqualTo(TICKET_1.getActive());
        Assertions.assertThat(ticket.getEventId()).isEqualTo(TICKET_1.getEventId());
        Assertions.assertThat(ticket.getEvent()).isEqualTo(EVENT_1);
    }

    @Test
    public void createTicket_WithInvalidData_ReturnsError() {
        when(eventClient.getEvent("0")).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThatThrownBy(() -> service.purchaseTicket(INVALID_TICKET_REPRESENTATION)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateEvent_WithValidData_ReturnsUpdatedEvent() {
        when(repository.findById(TICKET_1.getId())).thenReturn(Optional.of(TICKET_1_NO_EVENT));
        when(repository.save(any(Ticket.class))).thenReturn(TICKET_1);
        when(eventClient.getEvent(EVENT_2.getEventId())).thenReturn(EVENT_2);


        TicketSaveRequest updateRequest = new TicketSaveRequest("Pedro Silva", "321.654.987-00", "pedro.silva@email.com", EVENT_2.getEventId(), "200.00", "10.00");

        Ticket updatedTicket = service.update("1", updateRequest);

        Assertions.assertThat(updatedTicket.getCpf()).isEqualTo(TICKET_1.getCpf());
        Assertions.assertThat(updatedTicket.getCustomerName()).isEqualTo(TICKET_1.getCustomerName());
        Assertions.assertThat(updatedTicket.getCustomerMail()).isEqualTo(TICKET_1.getCustomerMail());
        Assertions.assertThat(updatedTicket.getBRLamount()).isEqualTo(TICKET_1.getBRLamount());
        Assertions.assertThat(updatedTicket.getUSDamount()).isEqualTo(TICKET_1.getUSDamount());
        Assertions.assertThat(updatedTicket.getStatus()).isEqualTo(TICKET_1.getStatus());
        Assertions.assertThat(updatedTicket.getActive()).isEqualTo(TICKET_1.getActive());
        Assertions.assertThat(updatedTicket.getEventId()).isEqualTo(TICKET_1.getEventId());
        Assertions.assertThat(updatedTicket.getEvent()).isEqualTo(EVENT_2);
    }

    @Test
    void getTicket_ByExistingId_ReturnsTicket() {
        when(repository.findById(TICKET_1.getId())).thenReturn(Optional.of(TICKET_1_NO_EVENT));
        when(eventClient.getEvent(anyString())).thenReturn(EVENT_1);

        Ticket ticket = service.findById(TICKET_1.getId());

        Assertions.assertThat(ticket.getCpf()).isEqualTo(TICKET_1.getCpf());
        Assertions.assertThat(ticket.getCustomerName()).isEqualTo(TICKET_1.getCustomerName());
        Assertions.assertThat(ticket.getCustomerMail()).isEqualTo(TICKET_1.getCustomerMail());
        Assertions.assertThat(ticket.getBRLamount()).isEqualTo(TICKET_1.getBRLamount());
        Assertions.assertThat(ticket.getUSDamount()).isEqualTo(TICKET_1.getUSDamount());
        Assertions.assertThat(ticket.getStatus()).isEqualTo(TICKET_1.getStatus());
        Assertions.assertThat(ticket.getActive()).isEqualTo(TICKET_1.getActive());
        Assertions.assertThat(ticket.getEventId()).isEqualTo(TICKET_1.getEventId());
        Assertions.assertThat(ticket.getEvent()).isEqualTo(EVENT_1);
    }

    @Test
    void getTicket_ByInvalidId_ReturnsEvent() {
        when(repository.findById(anyString())).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThatThrownBy(() -> service.findById(TICKET_1.getId())).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void softDeleteTicket_WithExistingId_doesNotThrowAnyException() {
        when(repository.findById(TICKET_1.getId())).thenReturn(Optional.of(TICKET_1_NO_EVENT));

        Assertions.assertThatCode(() -> service.softDelete(TICKET_1.getId())).doesNotThrowAnyException();

        Ticket ticket = service.findById("1");

        Assertions.assertThat(ticket.getActive()).isEqualTo(false);
    }

    @Test
    public void softDeleteTicket_WithUnexistingId_ThrowsException() {
        when(repository.findById(TICKET_1.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> service.softDelete(TICKET_1.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Ticket não encontrado.");
    }
}
