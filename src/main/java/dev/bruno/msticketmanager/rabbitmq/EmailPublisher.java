package dev.bruno.msticketmanager.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.bruno.msticketmanager.domain.Email;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class EmailPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.queues.email-queue}")
    private Queue queue;

    public EmailPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailToQueue(Email email) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            rabbitTemplate.convertAndSend(queue.getName(), mapper.writeValueAsString(email));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
