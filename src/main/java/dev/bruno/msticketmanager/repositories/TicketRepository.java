package dev.bruno.msticketmanager.repositories;

import dev.bruno.msticketmanager.domain.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TicketRepository extends MongoRepository<Ticket, String> {
}
