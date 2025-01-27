package dev.bruno.mseventmanager.services;

import dev.bruno.mseventmanager.client.ticket.TicketClient;
import dev.bruno.mseventmanager.client.viacep.ViaCepClient;
import dev.bruno.mseventmanager.client.viacep.ViaCepResponse;
import dev.bruno.mseventmanager.domain.Event;
import dev.bruno.mseventmanager.domain.EventCheck;
import dev.bruno.mseventmanager.domain.mapper.EventMapper;
import dev.bruno.mseventmanager.domain.representation.EventSaveRequest;
import dev.bruno.mseventmanager.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;
    private final TicketClient ticketClient;

    public Event save(EventSaveRequest eventSaveRequest) {
        ViaCepResponse cepInfo = viaCepClient.getCepInfo(eventSaveRequest.getCep());

        Event event = eventSaveRequest.toModel();
        event.setLogradouro(cepInfo.getLogradouro());
        event.setBairro(cepInfo.getBairro());
        event.setCidade(cepInfo.getLocalidade());
        event.setUf(cepInfo.getUf());

        return eventRepository.save(event);
    }

    public Event findById(String id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Não implementado")
        );
    }

    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Page<Event> findAllSorted(Pageable pageable) {
        Sort sort = Sort.by("eventName").ascending();
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return eventRepository.findAll(sortedPageable);
    }

    public Event update(String id, EventSaveRequest eventSaveRequest) {
        Event event = findById(id);
        ViaCepResponse cepInfo = viaCepClient.getCepInfo(eventSaveRequest.getCep());

        EventMapper.update(eventSaveRequest, event, cepInfo);

        return eventRepository.save(event);
    }

    public ResponseEntity<?> delete(String eventId) {
        EventCheck eventCheck = ticketClient.getEventCheck(eventId);

        if (eventCheck.getHasTickets()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header("Content-Type", "application/json")
                    .body(Map.of("error", "O evento não pode ser deletado porque possui ingressos vendidos."));
        }

        eventRepository.deleteById(eventId);
        return ResponseEntity.noContent().build();
    }
}
