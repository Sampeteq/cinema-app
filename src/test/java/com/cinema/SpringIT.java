package com.cinema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SpringIT {

    @Autowired
    private SqlDatabaseCleaner sqlDatabaseCleaner;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected WebTestClient webTestClient;

    @AfterEach
    void cleanDb() {
        sqlDatabaseCleaner.clean();
    }

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
