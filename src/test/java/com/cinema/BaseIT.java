package com.cinema;

import com.cinema.mail.domain.MailSenderPort;
import com.cinema.mail.domain.MailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Clock;

import static com.cinema.ClockFixtures.INSTANT;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@MockBean({MailService.class, MailSenderPort.class})
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
    private WebApplicationContext wac;

    protected WebTestClient webTestClient;

    @SpyBean
    private Clock clock;

    @BeforeEach
    protected void setUpMocks() {
        webTestClient = MockMvcWebTestClient
                .bindToApplicationContext(this.wac)
                .apply(springSecurity())
                .build();
        when(clock.instant()).thenReturn(INSTANT);
    }

    @AfterEach
    void cleanDb() {
        sqlDatabaseCleaner.clean();
    }
}
