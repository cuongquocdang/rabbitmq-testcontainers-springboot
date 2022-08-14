package com.example.rabbittestcontainers.contants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessagingConstant {

    public static final String QUEUE_SUFFIX = ".queue";
    public static final String PLQ_SUFFIX = ".plq";
    public static final String DLQ_SUFFIX = ".dlq";

    public static final String EMPTY_QUEUE_ERROR_MESSAGE = "queue name can not be null or empty";
}
