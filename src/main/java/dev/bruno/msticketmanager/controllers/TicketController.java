package dev.bruno.msticketmanager.controllers;

import dev.bruno.msticketmanager.domain.EventCheck;
import dev.bruno.msticketmanager.domain.Ticket;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;
import dev.bruno.msticketmanager.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tickets")
@Tag(name = "Tickets", description = "Gerenciamento de tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/create-ticket")
    @Operation(summary = "Criar um novo ticket", description = "Cria um ticket para o evento")
    public ResponseEntity<Ticket> create(@Valid @RequestBody TicketSaveRequest ticketPurchaseRequest) {
        return ResponseEntity.ok(ticketService.purchaseTicket(ticketPurchaseRequest));
    }

    @GetMapping("/get-ticket/{id}")
    @Operation(summary = "Buscar ticket por ID", description = "Busca um ticket pelo ID fornecido")
    public ResponseEntity<Ticket> findById(@PathVariable String id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @GetMapping("/check-tickets-by-event/{eventId}")
    @Operation(summary = "Verificar tickets de um evento", description = "Verifica a quantidade de tickets para um evento")
    public ResponseEntity<EventCheck> checkTicketsByEventId(@PathVariable String eventId) {
        return ResponseEntity.ok(ticketService.checkTicketsByEventId(eventId));
    }

    @PutMapping("/update-ticket/{id}")
    @Operation(summary = "Atualizar um ticket", description = "Atualiza as informações de um ticket existente")
    public ResponseEntity<Ticket> update(@PathVariable String id, @Valid @RequestBody TicketSaveRequest ticketSaveRequest) {
        return ResponseEntity.ok(ticketService.update(id, ticketSaveRequest));
    }

    @DeleteMapping("/cancel-ticket/{id}")
    @Operation(summary = "Cancelar um ticket", description = "Cancela um ticket existente com base no ID fornecido")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        ticketService.softDelete(id);

        return ResponseEntity.noContent().build();
    }
}
