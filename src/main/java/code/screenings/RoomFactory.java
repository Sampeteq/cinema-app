package code.screenings;

import code.screenings.dto.CreateRoomDto;
import code.screenings.dto.RoomDto;
import code.screenings.exception.RoomException;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
class RoomFactory {

    private final RoomRepository roomRepository;

    RoomDto createRoom(CreateRoomDto dto) {
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
