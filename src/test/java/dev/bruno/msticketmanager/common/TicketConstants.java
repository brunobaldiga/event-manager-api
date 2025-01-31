package dev.bruno.msticketmanager.common;

import dev.bruno.msticketmanager.domain.Ticket;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;

import static dev.bruno.msticketmanager.common.EventConstants.EVENT_1;
import static dev.bruno.msticketmanager.common.EventConstants.EVENT_2;

public class TicketConstants {
    public static final Ticket TICKET_1 = new Ticket("1", "12345678900", "João Silva", "joaosilva@gmail.com", "100.00", "20.00", "concluído", true, EVENT_1.getEventId(), EVENT_1);
    public static final Ticket TICKET_1_NO_EVENT = new Ticket("12345678900", "João Silva", "joaosilva@gmail.com", "100.00", "20.00", EVENT_1.getEventId(), "concluído");
    public static final TicketSaveRequest TICKET_1_REPRESENTATION = new TicketSaveRequest("João Silva", "12345678900", "joaosilva@gmail.com", EVENT_1.getEventId(), "100.00", "20.00");

    public static final Ticket TICKET_2 = new Ticket("2", "98765432100", "Maria Oliveira", "maria.oliveira@email.com", "150.00", "30.00", "pendente", true, EVENT_2.getEventId(), EVENT_2);
    public static final TicketSaveRequest TICKET_2_REPRESENTATION = new TicketSaveRequest("Maria Oliveira", "98765432100", "maria.oliveira@email.com", EVENT_2.getEventId(), "150.00", "30.00");


    public static final TicketSaveRequest INVALID_TICKET_REPRESENTATION = new TicketSaveRequest(
            "João Silva",
            "12345678900",
            "joaosilva@gmail.com",
            "0",
            "100.00",
            "20.00"
    );

    public static final TicketSaveRequest EMPTY_TICKET_REPRESENTATION = new TicketSaveRequest();
}
