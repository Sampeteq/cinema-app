package code.bookings.application.services;

import code.bookings.application.dto.CreateRoomDto;
import code.bookings.application.dto.RoomDto;
import code.bookings.domain.Room;
import code.bookings.domain.RoomRepository;
import code.bookings.infrastructure.exceptions.RoomNumberAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RoomCreateService {

    private final RoomRepository roomRepository;

    public RoomDto createRoom(CreateRoomDto dto) {
        if (roomRepository.existsByNumber(dto.number())) {
            throw new RoomNumberAlreadyExistsException();
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
