package code.rooms.application.queries;

import code.rooms.application.dto.RoomDto;
import code.rooms.application.dto.RoomMapper;
import code.rooms.domain.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RoomReadingService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<RoomDto> readAll() {
        return roomRepository
                .readAll()
                .stream()
                .map(roomMapper::mapToDto)
                .toList();
    }
}
