package code.rooms.application;

import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.application.dto.RoomDetails;
import code.rooms.application.dto.RoomDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class RoomSearcher {

    private final RoomRepository roomRepository;

    public List<RoomDto> searchAllRooms() {
        return roomRepository
                .findAll()
                .stream()
                .map(Room::toDto)
                .toList();
    }

    public Optional<RoomDetails> searchRoomDetails(UUID roomId) {
        return roomRepository
                .findById(roomId)
                .map(Room::toDetails);
    }
}
