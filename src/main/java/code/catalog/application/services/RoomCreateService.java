package code.catalog.application.services;

import code.catalog.application.dto.RoomCreateDto;
import code.catalog.domain.Room;
import code.catalog.domain.exceptions.RoomCustomIdAlreadyExistsException;
import code.catalog.domain.ports.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomCreateService {

    private final RoomRepository roomRepository;

    public void createRoom(RoomCreateDto dto) {
        if (roomRepository.existsByCustomId(dto.customId())) {
            throw new RoomCustomIdAlreadyExistsException();
        }
        var screeningRoom = Room.builder()
                .customId(dto.customId())
                .seatsInOneRowQuantity(dto.seatsQuantityInOneRow())
                .rowsQuantity(dto.rowsQuantity()).build();
       roomRepository.add(screeningRoom);
    }
}
