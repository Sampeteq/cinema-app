package code.bookings.application.services;

import code.bookings.application.dto.RoomDto;
import code.bookings.domain.Room;
import code.bookings.domain.RoomRepository;
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
