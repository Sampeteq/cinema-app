package code.rooms.application.commands;

import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import code.rooms.domain.exceptions.RoomNumberAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateRoomHandler {

    private final RoomRepository roomRepository;

    public void handle(CreateRoomCommand command) {
        if (roomRepository.existsByCustomId(command.customId())) {
            throw new RoomNumberAlreadyExistsException();
        }
        var screeningRoom = Room
                .builder()
                .customId(command.customId())
                .rowsQuantity(command.rowsQuantity())
                .seatsInOneRowQuantity(command.seatsQuantityInOneRow())
                .build();
       roomRepository.add(screeningRoom);
    }
}
