package code.rooms.infrastructure.rest;

import code.rooms.application.dto.RoomDto;
import code.rooms.application.handlers.RoomReadingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomReadingHandler roomReadingHandler;

    @GetMapping
    public List<RoomDto> searchAllRooms() {
        return roomReadingHandler.handle();
    }
}
