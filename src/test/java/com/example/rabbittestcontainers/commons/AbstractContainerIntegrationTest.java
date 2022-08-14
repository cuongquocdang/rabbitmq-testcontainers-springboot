package com.example.rabbittestcontainers.commons;

import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import static com.example.rabbittestcontainers.enums.RunningContainer.RABBIT_MQ;

public abstract class AbstractContainerIntegrationTest {

    protected static RabbitMQContainer RABBIT_MQ_CONTAINER;

    protected static void ensureRabbitMQRunning() {
        if (null == RABBIT_MQ_CONTAINER) {
            RABBIT_MQ_CONTAINER = new RabbitMQContainer(DockerImageName
                    .parse(RABBIT_MQ.getImage())
                    .withTag(RABBIT_MQ.getTag()));
        }

        if (!RABBIT_MQ_CONTAINER.isRunning()) {
            RABBIT_MQ_CONTAINER
                    .withExposedPorts(15672, 5672)
                    .start();
        }

        // mapping port
        System.setProperty("rabbitmq.port", String.valueOf(RABBIT_MQ_CONTAINER.getMappedPort(5672)));
    }
}
