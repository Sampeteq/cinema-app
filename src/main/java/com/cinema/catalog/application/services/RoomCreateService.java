package com.cinema.catalog.application.services;

import com.cinema.catalog.application.dto.RoomCreateDto;
import com.cinema.catalog.domain.Room;
import com.cinema.catalog.domain.exceptions.RoomCustomIdAlreadyExistsException;
import com.cinema.catalog.domain.ports.RoomRepository;
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
