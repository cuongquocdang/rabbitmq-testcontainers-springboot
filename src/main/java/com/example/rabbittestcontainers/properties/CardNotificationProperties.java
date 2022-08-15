package com.example.rabbittestcontainers.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "card-notification")
@ConstructorBinding
@AllArgsConstructor
@Getter
public class CardNotificationProperties {

    private boolean enabled;

    private int maxDeadThreshold;
    private int messageTimeToLive;

    private String notificationExchange;
    private String notificationHeader;
    private String deadLetterExchange;

    private QueueProcessingInfo statusChanged;

    @Getter
    @Setter
    public static class QueueProcessingInfo {

        private String queue;
        private String deadLetterQueue;
        private String parkingLotQueue;
        private String key;
    }
}
