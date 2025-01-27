package dev.bruno.msticketmanager.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue requestEventQueue() {
        return new Queue("request-event", true);
    }

    @Bean
    public Queue responseEventQueue() {
        return new Queue("response-event", true);
    }
}