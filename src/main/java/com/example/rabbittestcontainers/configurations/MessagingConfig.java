package com.example.rabbittestcontainers.configurations;

import com.example.rabbittestcontainers.properties.CardNotificationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MessagingConfig {

    private final CardNotificationProperties cardNotificationProperties;

    @Bean
    public HeadersExchange cardNotificationHeaderExchange() {
        return new HeadersExchange(cardNotificationProperties.getNotificationExchange());
    }

    @Bean
    public TopicExchange deadLetterTopicExchange() {
        return new TopicExchange(cardNotificationProperties.getDeadLetterExchange());
    }

    @Bean
    public Queue cardStatusChangedQueue() {
        return QueueBuilder
                .durable(cardNotificationProperties.getStatusChanged().getQueue())
                .deadLetterExchange(cardNotificationProperties.getDeadLetterExchange())
                .deadLetterRoutingKey(cardNotificationProperties.getStatusChanged().getDeadLetterQueue())
                .build();
    }

    @Bean
    public Queue cardStatusChangedDeadLetterQueue() {
        return QueueBuilder
                .durable(cardNotificationProperties.getStatusChanged().getDeadLetterQueue())
                .deadLetterExchange(cardNotificationProperties.getDeadLetterExchange())
                .deadLetterRoutingKey(cardNotificationProperties.getStatusChanged().getQueue())
                .ttl(cardNotificationProperties.getMessageTimeToLive())
                .build();
    }

    @Bean
    public Queue cardStatusChangedParkingLotQueue() {
        return QueueBuilder
                .durable(cardNotificationProperties.getStatusChanged().getParkingLotQueue())
                .build();
    }

    @Bean
    public Binding cardStatusChangedBinding(Queue cardStatusChangedQueue,
                                            HeadersExchange cardNotificationHeaderExchange) {
        return BindingBuilder
                .bind(cardStatusChangedQueue)
                .to(cardNotificationHeaderExchange)
                .where(cardNotificationProperties.getNotificationHeader())
                .matches(cardNotificationProperties.getStatusChanged().getKey());
    }

    @Bean
    public Declarables cardStatusChangedDeadLetterBindings(Queue cardStatusChangedQueue,
                                                           Queue cardStatusChangedDeadLetterQueue,
                                                           Queue cardStatusChangedParkingLotQueue,
                                                           TopicExchange deadLetterTopicExchange) {
        return new Declarables(
                BindingBuilder
                        .bind(cardStatusChangedQueue)
                        .to(deadLetterTopicExchange)
                        .with(cardStatusChangedQueue.getName()),
                BindingBuilder
                        .bind(cardStatusChangedDeadLetterQueue)
                        .to(deadLetterTopicExchange)
                        .with(cardStatusChangedDeadLetterQueue.getName()),
                BindingBuilder
                        .bind(cardStatusChangedParkingLotQueue)
                        .to(deadLetterTopicExchange)
                        .with(cardStatusChangedParkingLotQueue.getName())
        );
    }

}
