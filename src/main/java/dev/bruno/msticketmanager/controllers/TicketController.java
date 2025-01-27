package dev.bruno.msticketmanager.controllers;

import dev.bruno.msticketmanager.domain.EventCheck;
import dev.bruno.msticketmanager.domain.Ticket;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;
import dev.bruno.msticketmanager.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/create-ticket")
    public ResponseEntity<Ticket> create(@RequestBody TicketSaveRequest ticketPurchaseRequest) {
        return ResponseEntity.ok(ticketService.purchaseTicket(ticketPurchaseRequest));
    }

    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<Ticket> findById(@PathVariable String id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @GetMapping("/check-tickets-by-event/{eventId}")
    public ResponseEntity<EventCheck> checkTicketsByEventId(@PathVariable String eventId) {
        return ResponseEntity.ok(ticketService.checkTicketsByEventId(eventId));
    }

    @PutMapping("/update-ticket/{id}")
    public ResponseEntity<Ticket> update(@PathVariable String id, @RequestBody TicketSaveRequest ticketSaveRequest) {
        return ResponseEntity.ok(ticketService.update(id, ticketSaveRequest));
    }

    @DeleteMapping("/cancel-ticket/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        ticketService.softDelete(id);

        return ResponseEntity.noContent().build();
    }
}
