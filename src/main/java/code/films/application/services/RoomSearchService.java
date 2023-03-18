package code.films.application.services;

import code.films.application.dto.RoomDto;
import code.films.domain.Room;
import code.films.domain.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class RoomSearchService {

    private final RoomRepository roomRepository;

    public List<RoomDto> searchAllRooms() {
        return roomRepository
                .findAll()
                .stream()
                .map(Room::toDto)
                .toList();
    }
}
