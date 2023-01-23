package code.bookings;

import code.bookings.dto.CreateRoomDto;
import code.bookings.dto.RoomDto;
import code.bookings.exception.RoomException;
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
