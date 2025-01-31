package dev.bruno.mseventmanager.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bruno.mseventmanager.controllers.EventController;
import dev.bruno.mseventmanager.domain.Event;
import dev.bruno.mseventmanager.domain.representation.EventSaveRequest;
import dev.bruno.mseventmanager.exceptions.ResourceNotFoundException;
import dev.bruno.mseventmanager.services.EventService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static dev.bruno.mseventmanager.common.EventConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createEvent_WithValidData_ReturnsCreated() throws Exception {
        when(service.save(any(EventSaveRequest.class))).thenReturn(EVENT_1);

        mockMvc.perform(post("/events/create-event")
                        .content(mapper.writeValueAsString(EVENT_1_REPRESENTATION))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId").value(EVENT_1.getId()))
                .andExpect(jsonPath("$.eventName").value(EVENT_1.getEventName()))
                .andExpect(jsonPath("$.date").value(EVENT_1.getDate()))
                .andExpect(jsonPath("$.cep").value(EVENT_1.getCep()))
                .andExpect(jsonPath("$.logradouro").value(EVENT_1.getLogradouro()))
                .andExpect(jsonPath("$.bairro").value(EVENT_1.getBairro()))
                .andExpect(jsonPath("$.cidade").value(EVENT_1.getCidade()))
                .andExpect(jsonPath("$.uf").value(EVENT_1.getUf()));
    }

    @Test
    public void createEvent_WithBlankData_ReturnsNotFound() throws Exception {
        mockMvc.perform(post("/events/create-event")
                        .content(mapper.writeValueAsString(EMPTY_EVENT_REPRESENTATION))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createEvent_WithInvalidData_ReturnsNotFound() throws Exception {
        when(service.save(any(EventSaveRequest.class))).thenThrow(ResourceNotFoundException.class);


        mockMvc.perform(post("/events/create-event")
                        .content(mapper.writeValueAsString(INVALID_EVENT_REPRESENTATION))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findEventById_WithValidId_ReturnsEvent() throws Exception {
        when(service.findById(EVENT_1.getId())).thenReturn(EVENT_1);

        mockMvc.perform(get("/events/get-event/{id}", EVENT_1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(EVENT_1.getId()))
                .andExpect(jsonPath("$.eventName").value(EVENT_1.getEventName()));
    }

    @Test
    public void findAllEvents_ReturnsListOfEvents() throws Exception {
        Page<Event> eventPage = new PageImpl<>(List.of(EVENT_1, EVENT_2));

        Pageable pageable = PageRequest.of(0, 10);
        when(service.findAll(pageable)).thenReturn(eventPage);

        mockMvc.perform(get("/events/get-all-events")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    public void listEventsSorted_ReturnsSortedEvents() throws Exception {
        Page<Event> eventPage = new PageImpl<>(List.of(EVENT_1, EVENT_2));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("eventName").ascending());
        when(service.findAllSorted(pageable)).thenReturn(eventPage);

        mockMvc.perform(get("/events/get-all-events/sorted")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "eventName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].eventName").value(EVENT_1.getEventName()))
                .andExpect(jsonPath("$.content[1].eventName").value(EVENT_2.getEventName()));
    }

    @Test
    public void updateEvent_WithValidId_ReturnsUpdatedEvent() throws Exception {
        when(service.update(eq(EVENT_1.getId()), any(EventSaveRequest.class))).thenReturn(EVENT_2);

        mockMvc.perform(put("/events/update-event/{id}", EVENT_1.getId())
                        .content(mapper.writeValueAsString(EVENT_2_REPRESENTATION))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value(EVENT_2.getEventName()))
                .andExpect(jsonPath("$.date").value(EVENT_2.getDate()))
                .andExpect(jsonPath("$.logradouro").value(EVENT_2.getLogradouro()))
                .andExpect(jsonPath("$.bairro").value(EVENT_2.getBairro()))
                .andExpect(jsonPath("$.cidade").value(EVENT_2.getCidade()))
                .andExpect(jsonPath("$.uf").value(EVENT_2.getUf()));
    }

    @Test
    public void deleteEvent_WithValidId_ReturnsNoContent() throws Exception {
        when(service.delete(EVENT_1.getId())).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/events/delete-event/{id}", EVENT_1.getId()))
                .andExpect(status().isNoContent());
    }
}
