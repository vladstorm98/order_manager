package com.order_manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public abstract class BaseTest {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.4")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    @BeforeAll
    static void startContainer() {
        postgres.start();

        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }
}
