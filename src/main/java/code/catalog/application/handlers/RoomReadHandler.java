package code.catalog.application.handlers;

import code.catalog.application.dto.RoomDto;
import code.catalog.application.dto.RoomMapper;
import code.catalog.infrastructure.db.RoomRepository;
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
