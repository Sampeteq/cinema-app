package code.rooms.client.commands.handlers;

import code.rooms.client.commands.CreateRoomCommand;
import code.rooms.client.exceptions.RoomNumberAlreadyExistsException;
import code.rooms.domain.Room;
import code.rooms.domain.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
                .id(UUID.randomUUID())
                .customId(command.customId())
                .rowsQuantity(command.rowsQuantity())
                .seatsInOneRowQuantity(command.seatsQuantityInOneRow())
                .build();
       roomRepository.add(screeningRoom);
    }
}
