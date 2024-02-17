package com.cinema;

import com.cinema.mails.MailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

    @Autowired
    private SqlDatabaseCleaner sqlDatabaseCleaner;

    @Autowired
    protected WebTestClient webTestClient;

    @MockBean
    private MailService mailService;

    @BeforeEach
    protected void mockMailService() {
        doNothing().when(mailService).sendMail(any());
    }

    @AfterEach
    void cleanDb() {
        sqlDatabaseCleaner.clean();
    }
}
