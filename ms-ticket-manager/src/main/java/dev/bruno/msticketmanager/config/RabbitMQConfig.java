package dev.bruno.msticketmanager.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    @Value("${mq.queues.email-queue}")
    private String queue;

    @Bean
    public Queue emailQueue() {
        return new Queue(queue, true);
    }
}