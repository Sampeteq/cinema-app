package com.cinema;

import com.cinema.mails.domain.MailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Clock;

import static com.cinema.ClockFixtures.INSTANT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

    @ServiceConnection
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
