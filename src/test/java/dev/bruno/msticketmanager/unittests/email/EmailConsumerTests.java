package dev.bruno.msticketmanager.unittests.email;

import dev.bruno.msticketmanager.rabbitmq.EmailConsumer;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailConsumerTests {

    @InjectMocks
    private EmailConsumer emailConsumer;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        ReflectionTestUtils.setField(emailConsumer, "senderEmail", "no-reply@meusite.com");
    }

    @Test
    void consume_ValidEmail_SendsEmail() throws Exception {
        String jsonEmail = "{"
                + "\"to\": \"joao.pedro@gmail.com\","
                + "\"subject\": \"Assunto\","
                + "\"body\": \"Corpo do email\""
                + "}";

        emailConsumer.consume(jsonEmail);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
