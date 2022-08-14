package com.example.rabbittestcontainers;

import com.example.rabbittestcontainers.commons.AbstractContainerIntegrationTest;
import com.example.rabbittestcontainers.dtos.CardStatusChangedMessageDTO;
import com.example.rabbittestcontainers.enums.CardStatus;
import com.example.rabbittestcontainers.properties.CardNotificationProperties;
import com.example.rabbittestcontainers.services.CardStatusChangedPublisherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Slf4j
class CardStatusChangedNotificationIntegrationTest extends AbstractContainerIntegrationTest {

    @Autowired
    private CardNotificationProperties cardNotificationProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @BeforeAll
    public static void beforeAll() {
        ensureRabbitMQRunning();
    }

    @BeforeEach
    public void before() {
        sendCardStatusChangedMessage();
    }

    @Test
    void should_NotifyMessageSuccessfully_WhenReceivingCardStatusChangedMessage() {
        // TODO: implement test
        Assertions.assertTrue(RABBIT_MQ_CONTAINER.isRunning());
    }

    private CardStatusChangedMessageDTO sampleCardStatusChangedMessage() {
        return CardStatusChangedMessageDTO.builder()
                .uuid(UUID.randomUUID().toString())
                .timestamp(Instant.now().toString())
                .cardStatus(CardStatus.CANCELLED)
                .build();
    }

    @SneakyThrows
    private void sendCardStatusChangedMessage() {
        var cardStatusChangedProperties = cardNotificationProperties.getStatusChanged();

        var messageProperties = new MessageProperties();
        // populate header key
        messageProperties.setHeader(cardNotificationProperties.getHeader(), cardStatusChangedProperties.getKey());

        var messageConverted = new Message(objectMapper.writeValueAsBytes(sampleCardStatusChangedMessage()), messageProperties);

        // trigger point for rabbit listener - send message to header exchange
        rabbitTemplate.send(cardNotificationProperties.getExchange(), "", messageConverted);
    }

}
