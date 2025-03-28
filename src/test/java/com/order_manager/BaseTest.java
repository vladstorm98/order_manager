package com.order_manager;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseTest {

    @Autowired
    private Flyway flyway;

    @BeforeEach
    void cleanDatabase() {
        flyway.clean();
        flyway.migrate();
    }
}
