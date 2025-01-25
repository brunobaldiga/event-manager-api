package dev.bruno.msticketmanager.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${mq.queues.request-event-details}")
    private String queueRequestEventDetails;

    @Value("${mq.queues.response-event-details}")
    private String queueResponseEventDetails;

    @Bean
    public Queue requestQueue() {
        return new Queue(queueRequestEventDetails, true);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(queueResponseEventDetails, true);
    }
}