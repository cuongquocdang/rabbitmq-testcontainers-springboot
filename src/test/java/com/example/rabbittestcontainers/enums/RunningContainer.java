package com.example.rabbittestcontainers.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RunningContainer {
    RABBIT_MQ("rabbitmq", "3-management");

    private final String image;
    private final String tag;
}
