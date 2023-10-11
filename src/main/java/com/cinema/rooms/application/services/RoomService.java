package com.cinema.rooms.application.services;

import com.cinema.rooms.application.dto.RoomCreateDto;
import com.cinema.rooms.application.dto.RoomDto;
import com.cinema.rooms.application.dto.RoomMapper;
import com.cinema.rooms.domain.Room;
import com.cinema.rooms.domain.RoomRepository;
import com.cinema.rooms.domain.exceptions.RoomIdAlreadyExistsException;
import com.cinema.rooms.domain.exceptions.RoomsNoAvailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public void createRoom(RoomCreateDto dto) {
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

    @Transactional(readOnly = true)
    public RoomDto findFirstAvailableRoom(LocalDateTime start, LocalDateTime end) {
        return roomRepository
                .readAll()
                .stream()
                .filter(room -> room.hasNoOccupationOn(start, end))
                .findFirst()
                .map(roomMapper::toDto)
                .orElseThrow(RoomsNoAvailableException::new);
    }
}
