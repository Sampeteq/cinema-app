package com.cinema.rooms.application.commands.handlers;

import com.cinema.rooms.application.commands.CreateRoom;
import com.cinema.rooms.domain.Room;
import com.cinema.rooms.domain.RoomRepository;
import com.cinema.rooms.domain.exceptions.RoomIdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateRoomHandler {

    private final RoomRepository roomRepository;

    public void handle(CreateRoom command) {
        if (roomRepository.existsById(command.id())) {
            throw new RoomIdAlreadyExistsException();
        }
        var screeningRoom = new Room(
                command.id(),
                command.rowSeatsNumber(),
                command.rowsNumber()
        );
        roomRepository.add(screeningRoom);
    }
}
