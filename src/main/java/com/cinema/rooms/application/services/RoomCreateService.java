package com.cinema.rooms.application.services;

import com.cinema.rooms.application.dto.RoomCreateDto;
import com.cinema.rooms.domain.Room;
import com.cinema.rooms.domain.RoomRepository;
import com.cinema.rooms.domain.exceptions.RoomIdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RoomCreateService {

    private final RoomRepository roomRepository;

    void createRoom(RoomCreateDto dto) {
        if (roomRepository.existsById(dto.id())) {
            throw new RoomIdAlreadyExistsException();
        }
        var screeningRoom = new Room(
                dto.id(),
                dto.rowSeatsNumber(),
                dto.rowsNumber()
        );
       roomRepository.add(screeningRoom);
    }
}
