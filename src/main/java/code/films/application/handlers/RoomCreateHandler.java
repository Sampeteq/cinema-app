package code.films.application.handlers;

import code.films.domain.Room;
import code.films.domain.exceptions.RoomCustomIdAlreadyExistsException;
import code.films.infrastructure.db.RoomRepository;
import code.films.application.commands.RoomCreateCommand;
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
