package com.cinema;

import com.cinema.mails.MailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Clock;

import static com.cinema.ClockFixtures.CURRENT_DATE;
import static com.cinema.ClockFixtures.ZONE_OFFSET;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

    @Autowired
    private SqlDatabaseCleaner sqlDatabaseCleaner;

    @Autowired
    protected WebTestClient webTestClient;

    @MockBean
    private MailService mailService;

    @MockBean
    private Clock clock;

    @BeforeEach
    protected void setUpMocks() {
        doNothing().when(mailService).sendMail(any(), any(), any());
        when(clock.instant()).thenReturn(CURRENT_DATE);
        when(clock.getZone()).thenReturn(ZONE_OFFSET);
    }

    @AfterEach
    void cleanDb() {
        sqlDatabaseCleaner.clean();
    }
}
