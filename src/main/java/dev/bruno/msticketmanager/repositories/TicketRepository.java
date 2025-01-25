package dev.bruno.msticketmanager.repositories;

import dev.bruno.msticketmanager.domain.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByEventEventIdAndActiveTrue(String eventId);
}
