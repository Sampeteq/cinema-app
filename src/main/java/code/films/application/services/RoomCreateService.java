package code.films.application.services;

import code.films.application.dto.CreateRoomDto;
import code.films.application.dto.RoomDto;
import code.films.domain.Room;
import code.films.domain.RoomRepository;
import code.films.infrastructure.exceptions.RoomNumberAlreadyExistsException;
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
