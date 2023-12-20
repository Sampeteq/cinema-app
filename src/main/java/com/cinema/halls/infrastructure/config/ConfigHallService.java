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
            var json = resourceLoader
                    .getResource("classpath:" + hallsConfigFileName)
                    .getContentAsString(StandardCharsets.UTF_8);
            objectMapper
                    .readValue(json, new TypeReference<List<ConfigHallDto>>() {})
                    .forEach(createHallService::handle);
            log.info("Halls added");
        }
    }
}
