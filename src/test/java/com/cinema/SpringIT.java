package com.cinema;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SpringIT {

    @Autowired
    private SqlDatabaseCleaner sqlDatabaseCleaner;

    @Autowired
    protected WebTestClient webTestClient;

    @AfterEach
    void cleanDb() {
        sqlDatabaseCleaner.clean();
    }
}
