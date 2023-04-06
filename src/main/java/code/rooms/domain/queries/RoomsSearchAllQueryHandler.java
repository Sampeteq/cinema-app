package code.rooms.domain.queries;

import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RoomsSearchAllQueryHandler {

    private final RoomRepository roomRepository;

    public List<Room> handle() {
        return roomRepository.findAll();
    }
}
