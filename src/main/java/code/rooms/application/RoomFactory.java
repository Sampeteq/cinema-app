package code.rooms.application;

import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.domain.dto.CreateRoomDto;
import code.rooms.domain.dto.RoomDto;
import code.rooms.domain.exception.RoomException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class RoomFactory {

    private final RoomRepository roomRepository;

    public RoomDto createRoom(CreateRoomDto dto) {
        if (roomRepository.existsByNumber(dto.number())) {
            throw new RoomException("Screening room already exists: " + dto.number());
        }
        var screeningRoom = new Room(
                UUID.randomUUID(),
                dto.number(),
                dto.rowsQuantity(),
                dto.seatsQuantityInOneRow()
        );
        return roomRepository
                .save(screeningRoom)
                .toDto();
    }
}
