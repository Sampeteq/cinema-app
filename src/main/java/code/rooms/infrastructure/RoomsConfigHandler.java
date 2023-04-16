package code.rooms.infrastructure;

import code.rooms.domain.commands.handlers.RoomCreateCommandHandler;
import code.rooms.domain.queries.GetRoomsQueryHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class RoomsConfigHandler {

    private final GetRoomsQueryHandler getRoomsQueryHandler;

    private final RoomCreateCommandHandler roomCreateCommandHandler;

    @Value("${roomsConfigHandler.pathToRoomsConfig}")
    private String pathToRoomsConfig;

    @EventListener(ContextRefreshedEvent.class)
    public void handle() {
        if(getRoomsQueryHandler.handle().isEmpty()) {
            try {
                logIfFileNotExists();
                var json = readConfig();
                logIfFileIsEmpty(json);
                createRooms(json);
            }
            catch (IOException exception) {
                log.error(exception.getMessage());
            }
        }
    }

    private void logIfFileNotExists() {
        if (Files.notExists(Path.of(pathToRoomsConfig))) {
            log.warn("Rooms configs file not found");
        }
    }

    private String readConfig() throws IOException {
        return Files.readString(Path.of(pathToRoomsConfig));
    }

    private void logIfFileIsEmpty(String json) {
        if (json.isEmpty()) {
            log.info("Empty rooms config file");
        }
    }

    private void createRooms(String json) throws JsonProcessingException {
        var roomsFromConfig = new ObjectMapper().readValue(json, RoomsConfigDto.class);
        roomsFromConfig
                .rooms()
                .forEach(roomCreateCommandHandler::handle);
    }
}

