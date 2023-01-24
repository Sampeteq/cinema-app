package code.rooms;

import code.rooms.dto.RoomDetails;
import code.rooms.dto.RoomDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
class RoomSearcher {

    private final RoomRepository roomRepository;

    List<RoomDto> searchAllRooms() {
        return roomRepository
                .findAll()
                .stream()
                .map(Room::toDto)
                .toList();
    }

    Optional<RoomDetails> searchRoomDetails(UUID roomId) {
        return roomRepository
                .findById(roomId)
                .map(Room::toDetails);
    }
}
