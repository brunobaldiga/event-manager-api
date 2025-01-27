package dev.bruno.mseventmanager.controllers;

import dev.bruno.mseventmanager.domain.Event;
import dev.bruno.mseventmanager.domain.representation.EventSaveRequest;
import dev.bruno.mseventmanager.services.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("events")
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
        return ResponseEntity.ok(eventService.findById(id));
    }

    @GetMapping("/get-all-events")
    public ResponseEntity<Page<Event>> findAll(Pageable pageable) {
        Page<Event> events = eventService.findAll(pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/get-all-events/sorted")
    public ResponseEntity<Page<Event>> getAllEventsSorted(Pageable pageable) {
        Page<Event> events = eventService.findAllSorted(pageable);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/update-event/{id}")
    public ResponseEntity<Event> update(@PathVariable String id, @RequestBody EventSaveRequest eventSaveRequest) {
        Event event = eventService.update(id, eventSaveRequest);

        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return eventService.delete(id);
    }

}
