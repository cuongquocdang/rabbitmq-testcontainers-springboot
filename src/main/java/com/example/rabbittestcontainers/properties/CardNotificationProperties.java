package com.example.rabbittestcontainers.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.example.rabbittestcontainers.utils.MessagingUtil.*;

@ConfigurationProperties(prefix = "card-notification")
@ConstructorBinding
@AllArgsConstructor
@Getter
public class CardNotificationProperties {

    private int maxDeadThreshold;
    private int messageTimeToLive;

    private String exchange;
    private String header;
    private String deadLetterExchange;

    private QueueProcessingInfo statusChanged;

    @Getter
    @Setter
    public static class QueueProcessingInfo {

        private String queue;
        private String key;

        public String getQueueWithSuffix() {
            return formatQueue(queue);
        }

        public String getDLQWithSuffix() {
            return formatDLQ(queue);
        }

        public String getPLQWithSuffix() {
            return formatPLQ(queue);
        }
    }
}
