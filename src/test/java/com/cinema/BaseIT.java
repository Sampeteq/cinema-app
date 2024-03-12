package com.cinema;

import com.cinema.mails.MailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Clock;

import static com.cinema.ClockFixtures.INSTANT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            "postgres:latest"
    );

    static {
        postgresContainer.start();
    }

    @Autowired
    private SqlDatabaseCleaner sqlDatabaseCleaner;

    @Autowired
    protected WebTestClient webTestClient;

    @MockBean
    private MailService mailService;

    @SpyBean
    private Clock clock;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    protected void setUpMocks() {
        doNothing().when(mailService).sendMail(any(), any(), any());
        when(clock.instant()).thenReturn(INSTANT);
    }

    @AfterEach
    void cleanDb() {
        sqlDatabaseCleaner.clean();
    }
}
