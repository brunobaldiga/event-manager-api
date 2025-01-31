package dev.bruno.mseventmanager.controllers;

import dev.bruno.mseventmanager.domain.Event;
import dev.bruno.mseventmanager.domain.representation.EventSaveRequest;
import dev.bruno.mseventmanager.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("events")
@Tag(name = "Eventos", description = "Gerenciamento de eventos")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create-event")
    @Operation(summary = "Criar um novo evento", description = "Cria um evento com nome, data, hora e endereço")
    public ResponseEntity<Event> create(@RequestBody @Valid EventSaveRequest eventSaveRequest) {
        Event event = eventService.save(eventSaveRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/get-event/{id}")
                .buildAndExpand(event.getId())
                .toUri();

        return ResponseEntity.created(location).body(event);
    }

    @GetMapping("/get-event/{id}")
    @Operation(summary = "Obter evento por ID", description = "Recupera os detalhes de um evento com base no ID fornecido")
    public ResponseEntity<Event> findById(@PathVariable String id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @GetMapping("/get-all-events")
    @Operation(summary = "Obter todos os eventos", description = "Recupera todos os eventos cadastrados")
    public ResponseEntity<Page<Event>> findAll(Pageable pageable) {
        Page<Event> events = eventService.findAll(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/get-all-events/sorted")
    @Operation(summary = "Obter todos os eventos em ordem alfabética", description = "Recupera todos os eventos cadastrados")
    public ResponseEntity<Page<Event>> getAllEventsSorted(Pageable pageable) {
        Page<Event> events = eventService.findAllSorted(pageable);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/update-event/{id}")
    @Operation(summary = "Atualizar evento por ID", description = "Atualiza as informações de um evento com base no ID fornecido")
    public ResponseEntity<Event> update(@PathVariable String id, @RequestBody @Valid EventSaveRequest eventSaveRequest) {
        Event event = eventService.update(id, eventSaveRequest);

        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/delete-event/{id}")
    @Operation(summary = "Deletar evento por ID", description = "Deleta as informações de um evento com base no ID fornecido")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return eventService.delete(id);
    }

}
