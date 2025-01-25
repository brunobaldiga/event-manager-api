package dev.bruno.mseventmanager.services;

import dev.bruno.mseventmanager.client.ViaCepClient;
import dev.bruno.mseventmanager.client.ViaCepResponse;
import dev.bruno.mseventmanager.domain.Event;
import dev.bruno.mseventmanager.domain.representation.EventSaveRequest;
import dev.bruno.mseventmanager.repositories.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;

    public EventService(EventRepository eventRepository, ViaCepClient viaCepClient) {
        this.eventRepository = eventRepository;
        this.viaCepClient = viaCepClient;
    }

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

        event.setEventName(eventSaveRequest.getEventName());
        event.setDate(eventSaveRequest.getDateTime());
        event.setCep(eventSaveRequest.getCep());
        event.setLogradouro(cepInfo.getLogradouro());
        event.setBairro(cepInfo.getBairro());
        event.setCidade(cepInfo.getLocalidade());
        event.setUf(cepInfo.getUf());

        return eventRepository.save(event);
    }

    public void delete(String id) {
        eventRepository.deleteById(id);
    }
}
