package com.example.rabbittestcontainers.services;

import com.example.rabbittestcontainers.dtos.CardStatusChangedMessageDTO;
import com.example.rabbittestcontainers.enums.CardStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardStatusChangedPublisherService {

    public void publishCardStatusChangedMessage(CardStatusChangedMessageDTO cardStatusChangedMessage) {
        log.info("Service Bus - Send card status changed message: {}", cardStatusChangedMessage);
        if (CardStatus.DUMMY_ERROR == cardStatusChangedMessage.getCardStatus()) {
            throw new IllegalStateException("Failed to publish Service Bus!");
        }
    }

}
