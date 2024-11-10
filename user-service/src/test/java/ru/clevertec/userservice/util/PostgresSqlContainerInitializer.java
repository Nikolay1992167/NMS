package ru.clevertec.userservice.util;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresSqlContainerInitializer {

    private final static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.3");

    @DynamicPropertySource
    static void setUrl(DynamicPropertyRegistry registry) {
        container.start();
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }
}