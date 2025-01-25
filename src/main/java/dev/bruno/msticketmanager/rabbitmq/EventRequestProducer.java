package dev.bruno.msticketmanager.rabbitmq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bruno.msticketmanager.domain.Event;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class EventRequestProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.queues.request-event-details}")
    private Queue queueRequestEventDetails;

    private final BlockingQueue<Event> eventResponseQueue = new LinkedBlockingQueue<>();

    public EventRequestProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEventRequest(String eventId) {
        rabbitTemplate.convertAndSend(queueRequestEventDetails.getName(), eventId);
    }


    @RabbitListener(queues = "${mq.queues.response-event-details}")
    public void handleEventResponse(@Payload String payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Event event = mapper.readValue(payload, Event.class);
        eventResponseQueue.offer(event);
    }

    public Event getEventResponse() throws InterruptedException {
        return eventResponseQueue.poll(10, TimeUnit.SECONDS);
    }
}
