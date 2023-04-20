package code.rooms.client.commands.handlers;

import code.rooms.client.commands.CreateRoomCommand;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.infrastructure.exceptions.RoomNumberAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateRoomHandler {

    private final RoomRepository roomRepository;

    public void handle(CreateRoomCommand command) {
        if (roomRepository.existsByNumber(command.number())) {
            throw new RoomNumberAlreadyExistsException();
        }
        var screeningRoom = new Room(
                UUID.randomUUID(),
                command.number(),
                command.rowsQuantity(),
                command.seatsQuantityInOneRow()
        );
       roomRepository.save(screeningRoom);
    }
}
