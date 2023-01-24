package code.rooms;

import code.rooms.dto.CreateRoomDto;
import code.rooms.dto.RoomDetails;
import code.rooms.dto.RoomDto;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class RoomFacade {

    private final RoomFactory roomFactory;

    private final RoomSearcher roomSearcher;

    @Transactional
    public RoomDto createRoom(CreateRoomDto dto) {
        return roomFactory.createRoom(dto);
    }

    public List<RoomDto> searchAllRooms() {
        return roomSearcher.searchAllRooms();
    }

    public Optional<RoomDetails> searchRoomDetails(UUID roomId) {
        return roomSearcher.searchRoomDetails(roomId);
    }
}
