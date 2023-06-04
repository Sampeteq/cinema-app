package code.rooms.application.handlers;

import code.rooms.application.commands.RoomCreationCommand;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.domain.exceptions.RoomCustomIdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomCreationHandler {

    private final RoomRepository roomRepository;

    public void handle(RoomCreationCommand command) {
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
