package com.cinema.rooms.application.services;

import com.cinema.rooms.application.dto.RoomCreateDto;
import com.cinema.rooms.domain.RoomRepository;
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
class RoomConfigService {

    private final RoomRepository roomRepository;
    private final RoomCreateService roomCreateService;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Value("${rooms.roomsConfigFileName}")
    private String roomsConfigFileName;

    @EventListener(ContextRefreshedEvent.class)
    void createRoomsFromConfigOnStartUp() {
        if (roomRepository.count() == 0) {
            var json = readRoomsConfigAsJson();
            logIfFileIsEmpty(json);
            createRoomsFromJson(json);
            log.info("Rooms added");
        } else {
            log.info("Rooms already exists");
        }
    }

    private String readRoomsConfigAsJson() {
        try (
                var inputStream = resourceLoader
                .getResource("classpath:" + roomsConfigFileName)
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
            log.error("Empty rooms config file");
        }
    }

    private void createRoomsFromJson(String json) {
        try {
            objectMapper
                    .readValue(json, new TypeReference<List<RoomCreateDto>>() {
                    })
                    .forEach(roomCreateService::createRoom);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
