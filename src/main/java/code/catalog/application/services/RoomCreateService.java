package code.catalog.application.services;

import code.catalog.domain.Room;
import code.catalog.domain.exceptions.RoomCustomIdAlreadyExistsException;
import code.catalog.infrastructure.db.RoomRepository;
import code.catalog.application.dto.RoomCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomCreateService {

    private final RoomRepository roomRepository;

    public void createRoom(RoomCreateDto dto) {
        if (roomRepository.existsByCustomId(dto.customId())) {
            throw new RoomCustomIdAlreadyExistsException();
        }
        var screeningRoom = Room.create(
                dto.customId(),
                dto.rowsQuantity(),
                dto.seatsQuantityInOneRow()
        );
       roomRepository.add(screeningRoom);
    }
}
