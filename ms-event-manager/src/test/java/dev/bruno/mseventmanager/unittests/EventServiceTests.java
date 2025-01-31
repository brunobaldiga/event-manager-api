package dev.bruno.mseventmanager.unittests;

import dev.bruno.mseventmanager.client.ticket.TicketClient;
import dev.bruno.mseventmanager.client.viacep.ViaCepClient;
import dev.bruno.mseventmanager.client.viacep.ViaCepResponse;
import dev.bruno.mseventmanager.domain.Event;
import dev.bruno.mseventmanager.domain.EventCheck;
import dev.bruno.mseventmanager.domain.representation.EventSaveRequest;
import dev.bruno.mseventmanager.exceptions.ResourceNotFoundException;
import dev.bruno.mseventmanager.repositories.EventRepository;
import dev.bruno.mseventmanager.services.EventService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static dev.bruno.mseventmanager.common.EventConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EventServiceTests {
    @InjectMocks
    private EventService service;

    @Mock
    EventRepository repository;

    @Mock
    ViaCepClient viaCepClient;

    @Mock
    TicketClient ticketClient;

    private ViaCepResponse viaCepResponse;

    @BeforeEach
    void setUp() {
        viaCepResponse = new ViaCepResponse();
        viaCepResponse.setLogradouro("Avenida da França");
        viaCepResponse.setBairro("Comércio");
        viaCepResponse.setLocalidade("Salvador");
        viaCepResponse.setUf("BA");
    }

    @Test
    public void createEvent_WithValidData_ReturnsEvent() {
        when(viaCepClient.getCepInfo(anyString())).thenReturn(viaCepResponse);
        when(repository.save(any(Event.class))).thenReturn(EVENT_1);

        Event event = service.save(EVENT_1_REPRESENTATION);

        Assertions.assertThat(event.getLogradouro()).isEqualTo("Avenida da França");
        Assertions.assertThat(event.getBairro()).isEqualTo("Comércio");
        Assertions.assertThat(event.getCidade()).isEqualTo("Salvador");
        Assertions.assertThat(event.getUf()).isEqualTo("BA");
    }

    @Test
    public void createEvent_WithInvalidData_ReturnsError() {
        when(viaCepClient.getCepInfo(anyString())).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThatThrownBy(() -> service.save(INVALID_EVENT_REPRESENTATION)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getEvent_ByExistingId_ReturnsEvent() {
        when(repository.findById(EVENT_1.getId())).thenReturn(Optional.of(EVENT_1));

        Event event = service.findById("1");

        assertEquals("Feira Literária", event.getEventName());
        assertEquals("2024-10-05T10:00:00", event.getDate());
        assertEquals("40010-000", event.getCep());
        assertEquals("Avenida da França", event.getLogradouro());
        assertEquals("Comércio", event.getBairro());
        assertEquals("Salvador", event.getCidade());
        assertEquals("BA", event.getUf());
    }

    @Test
    void getEvent_ByInvalidId_ReturnsEvent() {
        when(repository.findById(anyString())).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThatThrownBy(() -> service.findById("1")).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listEvents_ReturnsAllEvents() {
        Page<Event> eventPage = new PageImpl<>(List.of(EVENT_1, EVENT_2));

        when(repository.findAll(any(Pageable.class))).thenReturn(eventPage);

        Page<Event> result = service.findAll(PageRequest.of(0, 2));

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(result.getContent()).hasSize(2);
        Assertions.assertThat(result.getContent().get(0)).isEqualTo(EVENT_1);
        Assertions.assertThat(result.getContent().get(1)).isEqualTo(EVENT_2);
    }

    @Test
    void listEvents_ReturnsNoEvents() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(Page.empty());

        Page<Event> result = service.findAll(pageable);

        Assertions.assertThat(result.getContent()).isEmpty();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void listEventsSorted_ReturnsSortedEvents() {
        Page<Event> eventPage = new PageImpl<>(List.of(EVENT_1, EVENT_2));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("eventName").ascending());
        when(repository.findAll(pageable)).thenReturn(eventPage);

        Page<Event> result = service.findAllSorted(pageable);

        Assertions.assertThat(result.getContent()).hasSize(2);
        Assertions.assertThat(result.getContent().get(0).getEventName()).isEqualTo("Feira Literária");
        Assertions.assertThat(result.getContent().get(1).getEventName()).isEqualTo("Festival de Música");
    }



    @Test
    public void removeEvent_WithExistingId_doesNotThrowAnyException() {
        when(repository.existsById(EVENT_1.getId())).thenReturn(true);
        when(ticketClient.getEventCheck(EVENT_1.getId())).thenReturn(new EventCheck(EVENT_1.getId(), false));

        Assertions.assertThatCode(() -> service.delete(EVENT_1.getId())).doesNotThrowAnyException();
    }

    @Test
    public void removeEvent_WithUnexistingId_ThrowsException() {
        when(repository.existsById("999")).thenReturn(false);
        when(ticketClient.getEventCheck("999")).thenReturn(new EventCheck("999", false));

        Assertions.assertThatThrownBy(() -> service.delete("999"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Evento não encontrado.");
    }

    @Test
    public void deleteEvent_WithTicketsSold_ReturnsConflict() {
        String eventId = "123";

        EventCheck eventCheck = new EventCheck(eventId, true);

        when(ticketClient.getEventCheck(eventId)).thenReturn(eventCheck);

        ResponseEntity<?> response = service.delete(eventId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).isInstanceOf(Map.class);
        assertThat(((Map<?, ?>) response.getBody()).get("error"))
                .isEqualTo("O evento não pode ser deletado porque possui ingressos vendidos.");
    }

    @Test
    public void updateEvent_WithValidData_ReturnsUpdatedEvent() {
        when(viaCepClient.getCepInfo(anyString())).thenReturn(viaCepResponse);
        when(repository.findById(anyString())).thenReturn(Optional.of(EVENT_1));
        when(repository.save(any(Event.class))).thenReturn(EVENT_1);

        EventSaveRequest updateRequest = new EventSaveRequest("Feira Literária Atualizada", "2024-10-06T12:00:00", "40010-001");

        Event updatedEvent = service.update("1", updateRequest);

        assertEquals("Feira Literária Atualizada", updatedEvent.getEventName());
        assertEquals("2024-10-06T12:00:00", updatedEvent.getDate());
        assertEquals("40010-001", updatedEvent.getCep());
        assertEquals("Avenida da França", updatedEvent.getLogradouro());
        assertEquals("Comércio", updatedEvent.getBairro());
        assertEquals("Salvador", updatedEvent.getCidade());
        assertEquals("BA", updatedEvent.getUf());
    }


}
