package com.cinema.rooms.application.services;

import com.cinema.rooms.application.dto.RoomCreateDto;
import com.cinema.rooms.domain.Room;
import com.cinema.rooms.domain.exceptions.RoomCustomIdAlreadyExistsException;
import com.cinema.rooms.domain.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RoomCreateService {

    private final RoomRepository roomRepository;

    void createRoom(RoomCreateDto dto) {
        if (roomRepository.existsByCustomId(dto.customId())) {
            throw new RoomCustomIdAlreadyExistsException();
        }
        var screeningRoom = new Room(
                dto.customId(),
                dto.rowSeatsNumber(),
                dto.rowsNumber()
        );
       roomRepository.add(screeningRoom);
    }
}
