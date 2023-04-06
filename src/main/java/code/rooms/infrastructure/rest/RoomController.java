package code.rooms.infrastructure.rest;

import code.rooms.domain.Room;
import code.rooms.domain.queries.RoomsSearchAllQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomsSearchAllQueryHandler roomsSearchAllQueryHandler;

    @GetMapping("/films/screenings/rooms")
    public List<RoomDto> searchAllRooms() {
        return roomsSearchAllQueryHandler
                .handle()
                .stream()
                .map(Room::toDto)
                .toList();
    }
}
