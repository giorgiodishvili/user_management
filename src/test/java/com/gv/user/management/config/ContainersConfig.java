package com.gv.user.management.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.utility.DockerImageName.parse;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {

    private static final String POSTGRES_IMAGE_NAME = "postgres:16-alpine";
    private static final String KAFKA_IMAGE_NAME = "confluentinc/cp-kafka:7.5.0";
    private static final String HAZELCAST_IMAGE_NAME = "hazelcast/hazelcast:latest";
    private static final String KAFKA_URL_PROPERTY = "spring.kafka.bootstrap-servers";
    private static final String HAZELCAST_URL_PROPERTY = "hazelcast.network.address";

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(parse(POSTGRES_IMAGE_NAME));
    }

    @Bean
    @ServiceConnection
    public KafkaContainer kafkaContainer() {
        KafkaContainer kafkaContainer = new KafkaContainer(parse(KAFKA_IMAGE_NAME));
        kafkaContainer.start();
        System.setProperty(KAFKA_URL_PROPERTY, kafkaContainer.getBootstrapServers());
        return kafkaContainer;
    }

    @Bean
    public GenericContainer<?> hazelcastContainer() {
        DockerImageName hazelcastImage = DockerImageName.parse(HAZELCAST_IMAGE_NAME);

        GenericContainer<?> container = new GenericContainer<>(hazelcastImage)
                .withExposedPorts(5701)
                .withStartupTimeout(java.time.Duration.ofMinutes(2));

        container.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (container.isRunning()) {
                container.stop();
            }
        }));

        String hazelcastAddress = container.getHost() + ":" + container.getMappedPort(5701);
        System.setProperty(HAZELCAST_URL_PROPERTY, hazelcastAddress);

        return container;
    }
}
