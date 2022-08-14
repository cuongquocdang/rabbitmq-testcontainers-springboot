package com.example.rabbittestcontainers.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "rabbitmq")
@ConstructorBinding
@AllArgsConstructor
@Getter
public class RabbitProperties {

    private int port;

    private String username;
    private String password;
    private String host;
    private String virtualHost;
}
