package code.rooms.application.services;

import code.rooms.application.dto.CreateRoomDto;
import code.rooms.application.dto.RoomDto;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.infrastructure.exceptions.RoomNumberAlreadyExistsException;
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
