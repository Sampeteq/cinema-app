package com.cinema.halls.infrastructure.config;

import com.cinema.halls.domain.HallRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
class ConfigHallService {

    private final HallRepository hallRepository;
    private final CreateHallHandler createHallHandler;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Value("${halls.hallsConfigFileName}")
    private String hallsConfigFileName;

    @EventListener(ContextRefreshedEvent.class)
    void createHallsFromConfigOnStartUp() {
        if (hallRepository.count() == 0) {
            var json = readHallsConfigAsJson();
            logIfFileIsEmpty(json);
            createHallsFromJson(json);
            log.info("Halls added");
        } else {
            log.info("HallsR already exists");
        }
    }

    private String readHallsConfigAsJson() {
        try (
                var inputStream = resourceLoader
                .getResource("classpath:" + hallsConfigFileName)
                .getInputStream()
        ) {
            var bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("IOException was thrown");
            throw new RuntimeException(e);
        }
    }

    private void logIfFileIsEmpty(String json) {
        if (json.isEmpty()) {
            log.error("Empty halls config file");
        }
    }

    private void createHallsFromJson(String json) {
        try {
            objectMapper
                    .readValue(json, new TypeReference<List<ConfigHallDto>>() {
                    })
                    .forEach(createHallHandler::handle);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
