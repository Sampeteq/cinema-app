package code.rooms.infrastructure;

import code.rooms.client.commands.CreateRoomCommands;
import code.rooms.client.commands.handlers.CreateRoomHandler;
import code.rooms.client.queries.GetRoomsQueryHandler;
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

    private final CreateRoomHandler createRoomHandler;

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
            log.info("Empty commands config file");
        }
    }

    private void createRooms(String json) throws JsonProcessingException {
        new ObjectMapper()
                .readValue(json, CreateRoomCommands.class)
                .commands()
                .forEach(createRoomHandler::handle);
    }
}

