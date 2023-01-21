package code.bookings;

import code.bookings.dto.RoomDto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class RoomSearcher {

    private final RoomRepository roomRepository;

    public List<RoomDto> searchAllRooms() {
        return roomRepository
                .findAll()
                .stream()
                .map(Room::toDto)
                .toList();
    }
}
