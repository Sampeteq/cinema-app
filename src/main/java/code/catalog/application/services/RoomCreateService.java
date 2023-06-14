package code.catalog.application.services;

import code.catalog.domain.Room;
import code.catalog.domain.exceptions.RoomCustomIdAlreadyExistsException;
import code.catalog.infrastructure.db.RoomRepository;
import code.catalog.application.commands.RoomCreateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomCreateService {

    private final RoomRepository roomRepository;

    public void createRoom(RoomCreateCommand command) {
        if (roomRepository.existsByCustomId(command.customId())) {
            throw new RoomCustomIdAlreadyExistsException();
        }
        var screeningRoom = Room.create(
                command.customId(),
                command.rowsQuantity(),
                command.seatsQuantityInOneRow()
        );
       roomRepository.add(screeningRoom);
    }
}
