package code.catalog.application.services;

import code.catalog.application.dto.RoomCreateDto;
import code.catalog.domain.ports.RoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
class RoomCreateConfigService {

    private final RoomRepository roomRepository;

    private final RoomCreateService roomCreateService;

    @Value("${rooms.pathToRoomsConfig}")
    private String pathToRoomsConfig;

    @EventListener(ContextRefreshedEvent.class)
    void createRoomsFromConfigOnStartUp() {
        if(roomRepository.readAll().isEmpty()) {
            try {
                logIfFileNotExists(pathToRoomsConfig);
                var json = readJsonFromRoomsConfig(pathToRoomsConfig);
                logIfFileIsEmpty(json);
                createRoomsFromJson(json);
            }
            catch (IOException exception) {
                log.error(exception.getMessage());
            }
        }
    }

    private void logIfFileNotExists(String pathToRoomsConfig) {
        if (Files.notExists(Path.of(pathToRoomsConfig))) {
            log.warn("Rooms configs file not found");
        }
    }

    private String readJsonFromRoomsConfig(String pathToRoomsConfig) throws IOException {
        return Files.readString(Path.of(pathToRoomsConfig));
    }

    private void logIfFileIsEmpty(String json) {
        if (json.isEmpty()) {
            log.info("Empty commands config file");
        }
    }

    private void createRoomsFromJson(String json) throws JsonProcessingException {
        new ObjectMapper()
                .readValue(json, new TypeReference<List<RoomCreateDto>>() {})
                .forEach(roomCreateService::createRoom);
    }
}
