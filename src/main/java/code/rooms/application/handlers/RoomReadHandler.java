package code.rooms.application.handlers;

import code.rooms.application.dto.RoomDto;
import code.rooms.application.dto.RoomMapper;
import code.rooms.domain.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RoomReadHandler {

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
