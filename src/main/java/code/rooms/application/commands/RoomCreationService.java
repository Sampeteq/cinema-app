package code.rooms.application.commands;

import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.domain.exceptions.RoomNumberAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomCreationService {

    private final RoomRepository roomRepository;

    public void createRoom(RoomCreationCommand command) {
        if (roomRepository.existsByCustomId(command.customId())) {
            throw new RoomNumberAlreadyExistsException();
        }
        var screeningRoom = Room.create(
                command.customId(),
                command.rowsQuantity(),
                command.seatsQuantityInOneRow()
        );
       roomRepository.add(screeningRoom);
    }
}
