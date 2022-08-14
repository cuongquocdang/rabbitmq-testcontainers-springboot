package com.example.rabbittestcontainers.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.Assert;

import static com.example.rabbittestcontainers.contants.MessagingConstant.*;

@UtilityClass
public class MessagingUtil {

    public static boolean containsSuffix(String queueName, String suffix) {
        return queueName.contains(suffix);
    }

    public static String formatQueue(String queueName) {
        Assert.hasLength(queueName, EMPTY_QUEUE_ERROR_MESSAGE);
        return containsSuffix(queueName, QUEUE_SUFFIX)
                ? queueName
                : queueName + QUEUE_SUFFIX;
    }

    public static String formatDLQ(String queueName) {
        Assert.hasLength(queueName, EMPTY_QUEUE_ERROR_MESSAGE);
        if (containsSuffix(queueName, DLQ_SUFFIX)) {
            return queueName;
        }
        return containsSuffix(queueName, QUEUE_SUFFIX)
                ? queueName.replace(QUEUE_SUFFIX, DLQ_SUFFIX)
                : queueName + DLQ_SUFFIX;
    }

    public static String formatPLQ(String queueName) {
        Assert.hasLength(queueName, EMPTY_QUEUE_ERROR_MESSAGE);
        if (containsSuffix(queueName, PLQ_SUFFIX)) {
            return queueName;
        }
        return containsSuffix(queueName, QUEUE_SUFFIX)
                ? queueName.replace(QUEUE_SUFFIX, PLQ_SUFFIX)
                : queueName + PLQ_SUFFIX;
    }
}
