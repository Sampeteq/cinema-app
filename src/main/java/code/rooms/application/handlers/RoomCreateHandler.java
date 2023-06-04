package code.rooms.application.handlers;

import code.rooms.application.commands.RoomCreateCommand;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.domain.exceptions.RoomCustomIdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomCreateHandler {

    private final RoomRepository roomRepository;

    public void handle(RoomCreateCommand command) {
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
