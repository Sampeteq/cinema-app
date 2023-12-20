package com.cinema.halls.infrastructure.config;

import com.cinema.halls.domain.HallRepository;
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
    private final ResourceLoader resourceLoader;
    @Value("${halls.hallsConfigFileName}")
    private String hallsConfigFileName;
    private final ObjectMapper objectMapper;
    private final CreateHallService createHallService;

    @EventListener(ContextRefreshedEvent.class)
    void createHallsFromConfigOnStartUp() throws IOException {
        if (hallRepository.count() == 0) {
            var json = readHallsConfigAsJson();
            logIfFileIsEmpty(json);
            createHallsFromJson(json);
            log.info("Halls added");
        } else {
            log.info("Halls already exists");
        }
    }

    private String readHallsConfigAsJson() throws IOException {
            var bytes = resourceLoader
                    .getResource("classpath:" + hallsConfigFileName + "e")
                    .getInputStream()
                    .readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
    }

    private void logIfFileIsEmpty(String json) {
        if (json.isEmpty()) {
            log.error("Empty halls config file");
        }
    }

    private void createHallsFromJson(String json) throws IOException {
            objectMapper
                    .readValue(json, new TypeReference<List<ConfigHallDto>>() {})
                    .forEach(createHallService::handle);
    }
}
