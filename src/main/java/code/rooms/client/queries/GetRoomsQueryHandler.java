package code.rooms.client.queries;

import code.rooms.client.dto.RoomDto;
import code.rooms.client.dto.mappers.RoomMapper;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GetRoomsQueryHandler {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<RoomDto> handle() {
        return roomRepository
                .readAll()
                .stream()
                .map(roomMapper::mapToDto)
                .toList();
    }
}
