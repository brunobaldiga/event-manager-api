package dev.bruno.msticketmanager.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bruno.msticketmanager.controllers.TicketController;
import dev.bruno.msticketmanager.domain.representation.TicketSaveRequest;
import dev.bruno.msticketmanager.exceptions.ResourceNotFoundException;
import dev.bruno.msticketmanager.services.TicketService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static dev.bruno.msticketmanager.common.EventConstants.EVENT_1;
import static dev.bruno.msticketmanager.common.EventConstants.EVENT_2;
import static dev.bruno.msticketmanager.common.TicketConstants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class EventControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createTicket_WithValidData_ReturnsCreated() throws Exception {
        when(service.purchaseTicket(any(TicketSaveRequest.class))).thenReturn(TICKET_1);

        mockMvc.perform(post("/tickets/create-ticket")
                        .content(mapper.writeValueAsString(TICKET_1_REPRESENTATION))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticketId").value(TICKET_1.getId()))
                .andExpect(jsonPath("$.cpf").value(TICKET_1.getCpf()))
                .andExpect(jsonPath("$.customerName").value(TICKET_1.getCustomerName()))
                .andExpect(jsonPath("$.customerMail").value(TICKET_1.getCustomerMail()))
                .andExpect(jsonPath("$.BRLtotalAmount").value(TICKET_1.getBRLamount()))
                .andExpect(jsonPath("$.USDtotalAmount").value(TICKET_1.getUSDamount()))
                .andExpect(jsonPath("$.status").value(TICKET_1.getStatus()))
                .andExpect(jsonPath("$.active").value(TICKET_1.getActive()))
                .andExpect(jsonPath("$.event.eventId").value(EVENT_1.getEventId()))
                .andExpect(jsonPath("$.event.eventName").value(EVENT_1.getEventName()))
                .andExpect(jsonPath("$.event.eventDateTime").value(EVENT_1.getDate()))
                .andExpect(jsonPath("$.event.logradouro").value(EVENT_1.getLogradouro()))
                .andExpect(jsonPath("$.event.bairro").value(EVENT_1.getBairro()))
                .andExpect(jsonPath("$.event.cidade").value(EVENT_1.getCidade()))
                .andExpect(jsonPath("$.event.uf").value(EVENT_1.getUf()));
    }

    @Test
    public void createTicket_WithBlankData_ReturnsNotFound() throws Exception {
        mockMvc.perform(post("/tickets/create-ticket")
                        .content(mapper.writeValueAsString(EMPTY_TICKET_REPRESENTATION))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createTicket_WithInvalidData_ReturnsNotFound() throws Exception {
        when(service.purchaseTicket(any(TicketSaveRequest.class))).thenThrow(ResourceNotFoundException.class);


        mockMvc.perform(post("/tickets/create-ticket")
                        .content(mapper.writeValueAsString(INVALID_TICKET_REPRESENTATION))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findTicket_WithValidId_ReturnsEvent() throws Exception {
        when(service.findById(TICKET_1.getId())).thenReturn(TICKET_1);

        mockMvc.perform(get("/tickets/get-ticket/{id}", TICKET_1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value(TICKET_1.getId()))
                .andExpect(jsonPath("$.cpf").value(TICKET_1.getCpf()))
                .andExpect(jsonPath("$.customerName").value(TICKET_1.getCustomerName()))
                .andExpect(jsonPath("$.customerMail").value(TICKET_1.getCustomerMail()))
                .andExpect(jsonPath("$.BRLtotalAmount").value(TICKET_1.getBRLamount()))
                .andExpect(jsonPath("$.USDtotalAmount").value(TICKET_1.getUSDamount()))
                .andExpect(jsonPath("$.status").value(TICKET_1.getStatus()))
                .andExpect(jsonPath("$.active").value(TICKET_1.getActive()))
                .andExpect(jsonPath("$.event.eventId").value(EVENT_1.getEventId()))
                .andExpect(jsonPath("$.event.eventName").value(EVENT_1.getEventName()))
                .andExpect(jsonPath("$.event.eventDateTime").value(EVENT_1.getDate()))
                .andExpect(jsonPath("$.event.logradouro").value(EVENT_1.getLogradouro()))
                .andExpect(jsonPath("$.event.bairro").value(EVENT_1.getBairro()))
                .andExpect(jsonPath("$.event.cidade").value(EVENT_1.getCidade()))
                .andExpect(jsonPath("$.event.uf").value(EVENT_1.getUf()));
    }

    @Test
    void findTicket_ByInvalidId_ReturnsEvent() {
        when(service.findById(anyString())).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThatThrownBy(() -> service.findById(TICKET_1.getId())).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void updateEvent_WithValidId_ReturnsUpdatedEvent() throws Exception {
        when(service.update(eq(TICKET_1.getId()), any(TicketSaveRequest.class))).thenReturn(TICKET_2);

        mockMvc.perform(put("/tickets/update-ticket/{id}", TICKET_1.getId())
                        .content(mapper.writeValueAsString(TICKET_2_REPRESENTATION))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.ticketId").value(TICKET_2.getId()))
                .andExpect(jsonPath("$.cpf").value(TICKET_2.getCpf()))
                .andExpect(jsonPath("$.customerName").value(TICKET_2.getCustomerName()))
                .andExpect(jsonPath("$.customerMail").value(TICKET_2.getCustomerMail()))
                .andExpect(jsonPath("$.BRLtotalAmount").value(TICKET_2.getBRLamount()))
                .andExpect(jsonPath("$.USDtotalAmount").value(TICKET_2.getUSDamount()))
                .andExpect(jsonPath("$.status").value(TICKET_2.getStatus()))
                .andExpect(jsonPath("$.active").value(TICKET_2.getActive()))
                .andExpect(jsonPath("$.event.eventId").value(EVENT_2.getEventId()))
                .andExpect(jsonPath("$.event.eventName").value(EVENT_2.getEventName()))
                .andExpect(jsonPath("$.event.eventDateTime").value(EVENT_2.getDate()))
                .andExpect(jsonPath("$.event.logradouro").value(EVENT_2.getLogradouro()))
                .andExpect(jsonPath("$.event.bairro").value(EVENT_2.getBairro()))
                .andExpect(jsonPath("$.event.cidade").value(EVENT_2.getCidade()))
                .andExpect(jsonPath("$.event.uf").value(EVENT_2.getUf()));
    }

    @Test
    public void softDeleteTicket_WithValidId_ReturnsNoContent() throws Exception {
        doNothing().when(service).softDelete(TICKET_1.getId());


        mockMvc.perform(delete("/tickets/cancel-ticket/{id}", TICKET_1.getId()))
                .andExpect(status().isNoContent());
    }
}
