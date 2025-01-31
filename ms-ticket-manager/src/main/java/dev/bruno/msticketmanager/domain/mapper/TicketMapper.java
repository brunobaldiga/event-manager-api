package dev.bruno.msticketmanager.domain.mapper;

import dev.bruno.msticketmanager.domain.Ticket;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketMapper {
    public static void update(TicketSaveRequest ticketSaveRequest, Ticket ticket) {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(TicketSaveRequest.class, Ticket.class)
                .addMappings(m -> m.skip(Ticket::setId))
                .addMappings(m -> m.skip(Ticket::setEvent));
        mapper.map(ticketSaveRequest, ticket);
    }
}
