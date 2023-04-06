package code.rooms.domain.queries;

import code.rooms.infrastructure.rest.RoomDto;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RoomsSearchAllQueryHandler {

    private final RoomRepository roomRepository;

    public List<RoomDto> handle() {
        return roomRepository
                .findAll()
                .stream()
                .map(Room::toDto)
                .toList();
    }
}
