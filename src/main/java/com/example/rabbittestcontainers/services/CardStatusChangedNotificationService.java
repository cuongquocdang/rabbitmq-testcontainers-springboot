package com.example.rabbittestcontainers.services;

import com.example.rabbittestcontainers.dtos.CardStatusChangedMessageDTO;
import com.example.rabbittestcontainers.properties.CardNotificationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@ConditionalOnProperty(prefix = "card-notification", name = "enabled", havingValue = "true")
@Slf4j
public class CardStatusChangedNotificationService {

    private final CardNotificationProperties cardNotificationProperties;
    private final CardStatusChangedPublisherService cardStatusChangedPublisherService;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    public CardStatusChangedNotificationService(CardNotificationProperties cardNotificationProperties,
                                                CardStatusChangedPublisherService cardStatusChangedPublisherService,
                                                ObjectMapper objectMapper,
                                                RabbitTemplate rabbitTemplate) {
        this.cardNotificationProperties = cardNotificationProperties;
        this.cardStatusChangedPublisherService = cardStatusChangedPublisherService;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${card-notification.status-changed.queue}")
    @SneakyThrows
    public void consumerCardStatusChangedMessage(Message receivedMessage) {
        var cardStatusChangedMessage =
                objectMapper.readValue(receivedMessage.getBody(), CardStatusChangedMessageDTO.class);
        var cardStatusChangedMessageProperties = receivedMessage.getMessageProperties();
        log.info("RabbitMQ - Received card status changed message: {} and properties: {}", cardStatusChangedMessage, cardStatusChangedMessageProperties);
        try {
            publishHandler(cardStatusChangedMessage);
        } catch (Exception exception) {
            log.error("An exception occurs while publishing message: {}", exception.getMessage());
            if (hasExceededRetryableCount(receivedMessage)) {
                // for monitoring
                sendBackToParkingLot(receivedMessage);
            } else {
                // retryable
                throw new AmqpRejectAndDontRequeueException(exception);
            }
        }
    }

    private void publishHandler(CardStatusChangedMessageDTO message) {
        cardStatusChangedPublisherService.publishCardStatusChangedMessage(message);
    }

    private String getParkingLotQueue() {
        return cardNotificationProperties.getStatusChanged().getParkingLotQueue();
    }

    private int getMaxDeadThreshold() {
        return cardNotificationProperties.getMaxDeadThreshold();
    }

    private boolean hasExceededRetryableCount(Message message) {
        var xDeathHeader = message.getMessageProperties().getXDeathHeader();
        if (xDeathHeader != null && !xDeathHeader.isEmpty()) {
            var deadCount = (Long) xDeathHeader.get(0).get("count");
            log.info("Current count from x dead header: {}", deadCount);
            var maxDeadThreshold = getMaxDeadThreshold();
            return deadCount >= maxDeadThreshold;
        }

        return false;
    }

    private void sendBackToParkingLot(Message failedMessage) {
        var deadLetterExchange = getDeadLetterExchange();
        var plqRouteKey = getParkingLotQueue();
        log.info("Retries exceeded send back message: {} and properties: {} to parking lot",
                new String(failedMessage.getBody(), StandardCharsets.UTF_8),
                failedMessage.getMessageProperties());
        rabbitTemplate.send(deadLetterExchange, plqRouteKey, failedMessage);
    }

    private String getDeadLetterExchange() {
        return cardNotificationProperties.getDeadLetterExchange();
    }
}
