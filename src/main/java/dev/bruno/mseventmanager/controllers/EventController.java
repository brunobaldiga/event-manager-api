package dev.bruno.mseventmanager.controllers;

import dev.bruno.mseventmanager.entities.Event;
import dev.bruno.mseventmanager.entities.representation.EventSaveRequest;
import dev.bruno.mseventmanager.services.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("events")
@Slf4j
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create-event")
    public ResponseEntity<Event> create(@RequestBody EventSaveRequest eventSaveRequest) {
        Event event = eventService.save(eventSaveRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/get-event/{id}")
                .buildAndExpand(event.getId())
                .toUri();
        return ResponseEntity.created(location).body(event);
    }

    @GetMapping("/get-event/{id}")
    public ResponseEntity<Event> findById(@PathVariable String id) {
        log.info("id {}", id);
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PutMapping("/update-event/{id}")
    public ResponseEntity<Event> update(@PathVariable String id, @RequestBody EventSaveRequest eventSaveRequest) {
        Event event = eventService.update(id, eventSaveRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/get-event/{id}")
                .buildAndExpand(event.getId())
                .toUri();

        return ResponseEntity.created(location).body(event);
    }

}
