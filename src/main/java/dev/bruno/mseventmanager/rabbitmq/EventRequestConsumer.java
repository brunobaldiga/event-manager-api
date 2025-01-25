package dev.bruno.mseventmanager.rabbitmq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bruno.mseventmanager.domain.Event;
import dev.bruno.mseventmanager.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventRequestConsumer {

    private final EventService eventService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.queues.response-event-details}")
    private Queue queueRequestEventDetails;

    @RabbitListener(queues = "${mq.queues.request-event-details}")
    public void handleEventRequest(String eventId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Event event = eventService.findById(eventId);

        if (event == null) {
            return;
        }

        rabbitTemplate.convertAndSend(queueRequestEventDetails.getName(), mapper.writeValueAsString(event));
    }
}
