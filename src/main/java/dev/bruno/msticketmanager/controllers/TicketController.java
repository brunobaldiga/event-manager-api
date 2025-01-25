package dev.bruno.msticketmanager.controllers;

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
}
