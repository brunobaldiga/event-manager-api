package dev.bruno.msticketmanager.unittests.email;

import dev.bruno.msticketmanager.domain.Email;
import dev.bruno.msticketmanager.rabbitmq.EmailPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailPublisherTests {

    @InjectMocks
    private EmailPublisher emailPublisher;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    Queue queue;

    @BeforeEach
    void setUp() {
        when(queue.getName()).thenReturn("email-queue");
    }

    @Test
    void sendEmailToQueue_ValidEmail_SendsMessage() {
        Email email = new Email("destinatario@email.com", "Assunto", "Corpo do email");

        emailPublisher.sendEmailToQueue(email);

        verify(rabbitTemplate, times(1)).convertAndSend(eq("email-queue"), anyString());
    }
}
