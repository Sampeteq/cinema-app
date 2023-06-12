package code.films.application.handlers;

import code.films.application.dto.RoomDto;
import code.films.application.dto.RoomMapper;
import code.films.infrastructure.db.RoomRepository;
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
