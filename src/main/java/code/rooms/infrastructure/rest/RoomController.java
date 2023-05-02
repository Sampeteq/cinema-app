package code.rooms.infrastructure.rest;

import code.rooms.client.dto.RoomDto;
import code.rooms.client.queries.GetRoomsQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final GetRoomsQueryHandler getRoomsQueryHandler;

    @GetMapping
    public List<RoomDto> searchAllRooms() {
        return getRoomsQueryHandler.handle();
    }
}
